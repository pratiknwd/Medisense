package com.example.mediscan.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediscan.BaseFragment
import com.example.mediscan.databinding.FragmentCriticalReportBinding
import com.example.mediscan.db.AppDatabase
import com.example.mediscan.db.dao.ReportDao
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class CriticalReportFragment : BaseFragment() {
    private var reportTypeId: Int by Delegates.notNull()
    private var _binding: FragmentCriticalReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var criticalReportsAdapter: CriticalReportsAdapter
    private lateinit var reportsDao: ReportDao
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportTypeId = requireArguments().getInt(REPORT_ID)
        criticalReportsAdapter = CriticalReportsAdapter()
        reportsDao = AppDatabase.getDatabase(requireContext().applicationContext).reportDao()
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCriticalReportBinding.inflate(inflater)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.criticalReportRV.apply {
            adapter = criticalReportsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        
        lifecycleScope.launch {
            val criticalReports = reportsDao.getCriticalReport(reportTypeId)
            criticalReportsAdapter.addReport(criticalReports)
        }
    }
    
    override fun onResume() {
        super.onResume()
    }
    
    override fun onNavigationIconClick(iconTapped: View?) {
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    
    companion object {
        const val FRAG_NAME = "CriticalReport"
        
        @JvmStatic
        fun newInstance(reportTypeId: Int) = CriticalReportFragment().apply {
            arguments = Bundle().apply {
                putInt(REPORT_ID, reportTypeId)
            }
        }
    }
}