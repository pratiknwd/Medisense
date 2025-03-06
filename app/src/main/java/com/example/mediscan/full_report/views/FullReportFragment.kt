package com.example.mediscan.full_report.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mediscan.BaseFragment
import com.example.mediscan.MainActivity
import com.example.mediscan.R

class FullReportFragment : BaseFragment() {
    override fun onNavigationIconClick(iconTapped: View?) {
    
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_full_report, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fullReportRV: RecyclerView = view.findViewById(R.id.fullReportRV)
        fullReportRV.layoutManager = LinearLayoutManager(requireContext())
        fullReportRV.adapter = ReportRV()
    }
    
    override fun onResume() {
        super.onResume()
        initToolbar((activity as MainActivity).toolbar, "Full Report")
    }
    
    companion object {
        val FRAG_NAME = "FullReport"
        
        @JvmStatic
        fun newInstance() = FullReportFragment()
    }
}