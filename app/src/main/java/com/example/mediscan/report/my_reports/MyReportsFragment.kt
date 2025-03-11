package com.example.mediscan.report.my_reports

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mediscan.BaseFragment
import com.example.mediscan.MainActivity
import com.example.mediscan.PDFUtil
import com.example.mediscan.auth.PEF_USER_ID
import com.example.mediscan.auth.SHARED_PREF_NAME
import com.example.mediscan.databinding.FragmentMyReportsBinding
import com.example.mediscan.db.AppDatabase
import com.example.mediscan.db.dao.ReportTypeDao
import kotlinx.coroutines.launch

class MyReportsFragment : BaseFragment() {
    private var _binding: FragmentMyReportsBinding? = null
    private val binding get() = _binding!!
    private lateinit var myReportsAdapter: MyReportsAdapter
    private lateinit var reportTypeDao: ReportTypeDao
    
    
    override fun onNavigationIconClick(iconTapped: View?) {
    
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db by lazy { AppDatabase.getDatabase(requireContext()) }
        reportTypeDao = db.reportTypeDao()
        myReportsAdapter = MyReportsAdapter(
            { reportTypeId -> (requireActivity() as MainActivity).openFullReportFragment(reportTypeId) },
            { reportTypeId -> onDownloadReportClick(reportTypeId) }
        )
    }
    
    private fun onDownloadReportClick(reportTypeId: Int) {
        Toast.makeText(requireContext(), "pdf download initialized", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            val reportsDao = AppDatabase.getDatabase(requireContext()).reportDao()
            val criticalReports = reportsDao.getCriticalReport(reportTypeId)
            PDFUtil.createReportPDF(requireContext(), criticalReports)
        }
    }
    
    private fun getUserId(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(PEF_USER_ID, -1)  // Default value -1 if not found
    }
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMyReportsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myReportsRV.adapter = myReportsAdapter
        binding.myReportsRV.layoutManager = LinearLayoutManager(requireContext())
        lifecycleScope.launch {
            val reports = reportTypeDao.getReportTypesByUser(getUserId(requireContext()))
            Log.d("9155881234", "reports: $reports")
            myReportsAdapter.updateList(reports)
        }
    }
    
    override fun onResume() {
        super.onResume()
        initToolbar((activity as MainActivity).toolbar, "My Reports")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        const val FRAG_NAME = "MyReports"
        
        @JvmStatic
        fun newInstance() = MyReportsFragment()
    }
}