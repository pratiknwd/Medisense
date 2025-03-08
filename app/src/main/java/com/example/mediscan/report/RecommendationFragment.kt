package com.example.mediscan.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediscan.BaseFragment
import com.example.mediscan.databinding.FragmentRecommendationBinding
import com.example.mediscan.db.AppDatabase
import com.example.mediscan.db.dao.ReportDao
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class RecommendationFragment : BaseFragment() {
    private var _binding: FragmentRecommendationBinding? = null
    private val binding get() = _binding!!
    private var reportTypeId: Int by Delegates.notNull()
    private lateinit var reportsDao: ReportDao
    private lateinit var recommendationReportAdapter: RecommendationReportAdapter
    
    
    override fun onNavigationIconClick(iconTapped: View?) {
    
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            reportTypeId = it.getInt(REPORT_ID)
        }
        reportsDao = AppDatabase.getDatabase(requireContext().applicationContext).reportDao()
        recommendationReportAdapter = RecommendationReportAdapter()
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecommendationBinding.inflate(inflater)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recommendationRV.apply {
            adapter = recommendationReportAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        
        lifecycleScope.launch {
            val recommendationReports = reportsDao.getCriticalReport(reportTypeId)
            recommendationReportAdapter.updateData(recommendationReports)
        }
    }
    
    override fun onResume() {
        super.onResume()
        
        
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    
    companion object {
        const val FRAG_NAME = "RecommendationFrag"
        
        @JvmStatic
        fun newInstance(reportTypeId: Int) = RecommendationFragment().apply {
            arguments = Bundle().apply {
                putInt(REPORT_ID, reportTypeId)
            }
        }
    }
}