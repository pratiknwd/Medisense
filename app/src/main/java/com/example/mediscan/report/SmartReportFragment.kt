package com.example.mediscan.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mediscan.BaseFragment
import com.example.mediscan.MainActivity
import com.example.mediscan.databinding.FragmentSmartReportBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.properties.Delegates


const val REPORT_ID = "REPORT_ID"

class SmartReportFragment : BaseFragment() {
    private var reportTypeId: Int by Delegates.notNull()
    private var _binding: FragmentSmartReportBinding? = null
    private val binding get() = _binding!!
    private lateinit var smartReportVP2Adapter: SmartReportViewPagerAdapter
    
    override fun onNavigationIconClick(iconTapped: View?) {
    
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireArguments().let {
            reportTypeId = it.getInt(REPORT_ID)
        }
        
        
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSmartReportBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        smartReportVP2Adapter = SmartReportViewPagerAdapter(reportTypeId, this)
        binding.smartReportVP2.offscreenPageLimit=2
        binding.smartReportVP2.adapter = smartReportVP2Adapter
        
        TabLayoutMediator(binding.smartReportTabLayout, binding.smartReportVP2) { tab, position ->
            tab.text = when (position) {
                0 -> "    Critical    "
                1 -> "   Full Report   "
                2 -> " Recommendation "
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }.attach()
        
    }
    
    override fun onResume() {
        super.onResume()
        initToolbar((activity as MainActivity).toolbar, "Smart Report")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        const val FRAG_NAME = "SmartReport"
        
        @JvmStatic
        fun newInstance(reportTypeId: Int) =
            SmartReportFragment().apply {
                arguments = Bundle().apply {
                    putInt(REPORT_ID, reportTypeId)
                }
            }
    }
}