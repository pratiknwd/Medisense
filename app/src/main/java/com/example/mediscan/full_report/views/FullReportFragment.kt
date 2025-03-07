package com.example.mediscan.full_report.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.BaseFragment
import com.example.mediscan.MainActivity
import com.example.mediscan.R
import com.example.mediscan.db.AppDatabase
import com.example.mediscan.db.dao.ReportDao
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

private const val FULL_REPORT_ID = "FULL_REPORT_ID"

class FullReportFragment : BaseFragment() {
    private var reportTypeId by Delegates.notNull<Int>()
    private lateinit var reportDao: ReportDao
    private lateinit var fullReportAdapter: ReportRV
    
    override fun onNavigationIconClick(iconTapped: View?) {
    
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportTypeId = requireArguments().getInt(FULL_REPORT_ID)
        reportDao = AppDatabase.getDatabase(requireContext().applicationContext).reportDao()
        fullReportAdapter = ReportRV()
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_full_report, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fullReportRV: RecyclerView = view.findViewById(R.id.fullReportRV)
        fullReportRV.layoutManager = LinearLayoutManager(requireContext())
        fullReportRV.adapter = fullReportAdapter
        
        lifecycleScope.launch {
            val reports = reportDao.getReportsByType(reportTypeId)
            fullReportAdapter.addReport(reports)
        }
    }
    
    override fun onResume() {
        super.onResume()
        initToolbar((activity as MainActivity).toolbar, "Full Report")
        
        Toast.makeText(requireContext(), "reportTypeId = $reportTypeId", Toast.LENGTH_SHORT).show()
    }
    
    companion object {
        val FRAG_NAME = "FullReport"
        
        @JvmStatic
        fun newInstance(reportTypeId: Int) = FullReportFragment().apply {
                arguments = Bundle().apply {
                    putInt(FULL_REPORT_ID, reportTypeId)
                }
            }
    }
}