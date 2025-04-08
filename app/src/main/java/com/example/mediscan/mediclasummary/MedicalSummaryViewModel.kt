package com.example.mediscan.mediclasummary

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mediscan.SharedViewModel
import com.example.mediscan.auth.PEF_USER_ID
import com.example.mediscan.auth.SHARED_PREF_NAME
import com.example.mediscan.db.AppDatabase
import com.example.mediscan.db.dao.MedicalSummaryDao
import com.example.mediscan.db.entity.MedicalSummary
import com.example.mediscan.db.entity.Report
import com.example.mediscan.db.entity.ReportType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MedicalSummaryViewModel(application: Application) : AndroidViewModel(application) {

    private val medicalSummaryDao: MedicalSummaryDao?
    private lateinit var sharedViewModel: SharedViewModel

    private val _medicalSummary = MutableLiveData<String?>()
    val medicalSummary: LiveData<String?> = _medicalSummary

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _summarySaved = MutableLiveData<Boolean?>()
    val summarySaved: LiveData<Boolean?> = _summarySaved

    init {
        val db = AppDatabase.getDatabase(application)
        medicalSummaryDao = db.medicalSummaryDao()
    }

    fun setSharedViewModel(sharedViewModel: SharedViewModel) {
        this.sharedViewModel = sharedViewModel
    }

    fun getMedicalSummaryWithDetails(reportsWithDetails: List<Pair<ReportType, Report>>) {
        _isLoading.postValue(true)
        _errorMessage.postValue(null)

        val userId = getUserIdFromPrefs()
        val reportHash = reportsWithDetails.hashCode() // Consider a better hash if order matters

        viewModelScope.launch(Dispatchers.IO) {
            val storedSummary = medicalSummaryDao?.getSummaryByUserAndReportHash(userId, reportHash)
            Dispatchers.Main.run {
                if (storedSummary != null) {
                    _isLoading.postValue(false)
                    _medicalSummary.postValue(storedSummary.summaryText)
                    Log.d("MedicalSummaryViewModel", "Fetched summary from DB")
                } else {
                    Log.d("MedicalSummaryViewModel", "No summary found in DB, generating new one.")
                    generateAndSaveSummaryWithDetails(reportsWithDetails)
                }
            }
        }
    }

    private fun generateAndSaveSummaryWithDetails(reportsWithDetails: List<Pair<ReportType, Report>>) {
        val prompt = buildSummaryPromptWithDetails(reportsWithDetails)
        val userId = getUserIdFromPrefs()
        val reportHash = reportsWithDetails.hashCode()

        sharedViewModel.getMedicalSummary(prompt) { response ->
            _isLoading.postValue(false)
            if (response != null) {
                _medicalSummary.postValue(response)
                saveMedicalSummary(response, reportHash, userId)
            } else {
                _errorMessage.postValue("Failed to generate medical summary.")
            }
        }
    }

    private fun buildSummaryPromptWithDetails(reportsWithDetails: List<Pair<ReportType, Report>>): String {
        val promptBuilder = StringBuilder("Summarize the following critical medical findings. For each report type, start with the 'Report Name: [ReportTypeName] (Uploaded: [FormattedDate])' followed by a concise summary of the critical findings:\n\n")
        if (reportsWithDetails.isEmpty()) {
            promptBuilder.append("No critical findings found in the recent report types.\n")
        } else {
            val sdf = SimpleDateFormat("d MMM yyyy", Locale.getDefault()) // Include year for clarity
            val reportsByType = reportsWithDetails.groupBy { it.first.reportTypeName }

            reportsByType.forEach { (reportTypeName, reportsForType) ->
                val firstReportType = reportsForType.first().first
                val formattedDate = sdf.format(Date(firstReportType.timestamp))

                promptBuilder.append("Report Name: $reportTypeName (Uploaded: $formattedDate)\n")

                val criticalFindings = StringBuilder()
                reportsForType.forEach { (_, report) ->
                    criticalFindings.append("${report.testName}: ${report.testValue}")
                    report.lowerLimit?.let { criticalFindings.append(" (Lower: $it)") }
                    report.upperLimit?.let { criticalFindings.append(" (Upper: $it)") }
                    criticalFindings.append("; ")
                }
                promptBuilder.append("Critical Findings: $criticalFindings\n")
                promptBuilder.append("Summary: ") // Indicate where the concise summary should start
                promptBuilder.append("---\n\n") // Separator
            }
        }
        promptBuilder.append("Finally, provide an overall very concise summary of the most significant implications across all report types mentioned above.")
        return promptBuilder.toString()
    }

    private fun saveMedicalSummary(summaryText: String, reportHash: Int, userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val summary = MedicalSummary(userId = userId, summaryText = summaryText, reportHash = reportHash)
                medicalSummaryDao?.insertSummary(summary)
                Dispatchers.Main.run {
                    _summarySaved.postValue(true)
                    Log.d("MedicalSummaryViewModel", "Summary saved to DB for user $userId and hash $reportHash")
                }
            } catch (e: Exception) {
                Dispatchers.Main.run {
                    _summarySaved.postValue(false)
                    _errorMessage.postValue("Failed to save medical summary.")
                }
                e.printStackTrace()
            }
        }
    }

    private fun getUserIdFromPrefs(): Int {
        val sharedPreferences = getApplication<Application>().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(PEF_USER_ID, -1)
    }

    fun resetSummarySavedStatus() {
        _summarySaved.value = null
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }
}