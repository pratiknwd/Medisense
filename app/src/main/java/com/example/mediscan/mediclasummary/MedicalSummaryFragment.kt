package com.example.mediscan.mediclasummary

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mediscan.BaseFragment
import com.example.mediscan.MainActivity
import com.example.mediscan.SharedViewModel
import com.example.mediscan.auth.PEF_USER_ID
import com.example.mediscan.auth.SHARED_PREF_NAME
import com.example.mediscan.databinding.FragmentMedicalSummaryBinding
import com.example.mediscan.db.AppDatabase
import com.example.mediscan.db.dao.ReportDao
import com.example.mediscan.db.dao.ReportTypeDao
import com.example.mediscan.db.entity.Report
import com.example.mediscan.db.entity.ReportType
import kotlinx.coroutines.launch


class MedicalSummaryFragment : BaseFragment() {

    private var _binding: FragmentMedicalSummaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var reportTypeDao: ReportTypeDao
    private lateinit var reportsDao: ReportDao
    private lateinit var viewModel: MedicalSummaryViewModel
    private lateinit var sharedViewModel: SharedViewModel // Get instance from Activity

    override fun onNavigationIconClick(iconTapped: View?) {
        // Handle navigation icon click if needed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportTypeDao = AppDatabase.getDatabase(requireContext().applicationContext).reportTypeDao()
        reportsDao = AppDatabase.getDatabase(requireContext().applicationContext).reportDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicalSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MedicalSummaryViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.setSharedViewModel(sharedViewModel) // Provide the SharedViewModel instance

        binding.progressBar.visibility = View.VISIBLE
        binding.summaryTextView.visibility = View.GONE

        lifecycleScope.launch {
            val userId = getUserId(requireContext())
            val allReportTypes = reportTypeDao.getReportTypesByUser(userId)
            val top5ReportTypes = allReportTypes.take(5)
            val reportsWithDetails = mutableListOf<Pair<ReportType, Report>>()

            for (reportType in top5ReportTypes) {
                val criticalReportsForType = reportsDao.getCriticalReport(reportType.reportTypeId)
                criticalReportsForType.forEach { report ->
                    reportsWithDetails.add(Pair(reportType, report))
                }
            }

            viewModel.medicalSummary.observe(viewLifecycleOwner) { summary ->
                binding.progressBar.visibility = View.GONE
                binding.summaryTextView.visibility = View.VISIBLE
                if (summary != null) {
                    binding.summaryTextView.text = summary
                } else {

                    if (viewModel.isLoading.value == true) {
                        binding.summaryTextView.text = "Loading summary..."
                    } else if (viewModel.errorMessage.value != null) {
                        binding.summaryTextView.text = viewModel.errorMessage.value
                    } else {
                        binding.summaryTextView.text = "Failed to load or generate summary."
                    }
                }
            }

            // Trigger the process to get the medical summary (checks DB first)
            viewModel.getMedicalSummaryWithDetails(reportsWithDetails)
        }

        // Observe error messages from ViewModel (for other errors)
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                viewModel.resetErrorMessage()
            }
        }
    }

    private fun getUserId(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(PEF_USER_ID, -1)  // Default value -1 if not found
    }

    override fun onResume() {
        super.onResume()
        initToolbar((activity as MainActivity).toolbar, "Recent Critical Report Summary")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val FRAG_NAME = "MedicalSummaryFrag"

        @JvmStatic
        fun newInstance() = MedicalSummaryFragment()
    }
}