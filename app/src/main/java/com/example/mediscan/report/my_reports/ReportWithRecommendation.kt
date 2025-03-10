import com.example.mediscan.SharedViewModel
import com.example.mediscan.db.entity.Report

data class ReportWithRecommendation(
    val reportId: Int,
    val testName: String?,
    val testValue: Float?,
    val unit: String?,
    val upperLimit: Float?,
    val lowerLimit: Float?,
    val recommendation: String?, // New field for storing LLM response
)

suspend fun getRecommendationsForReports(
    reports: List<Report>,
    sharedViewModel: SharedViewModel
): List<ReportWithRecommendation> {
    val enrichedReports = mutableListOf<ReportWithRecommendation>()

    for (report in reports) {
        val query = """
            You are a medical doctor. This is a user's test result:
            - Test Name: ${report.testName}
            - Test Value: ${report.testValue} ${report.unit ?: ""}
            - Normal Range: ${report.lowerLimit} - ${report.upperLimit}
            
            Give a recommendation on how to improve organically, including diet recommendations and home remedies.
            Provide an answer in around 50 words.
        """.trimIndent()

        val recommendation = sharedViewModel.getResponseFromGemini(query) ?: "No recommendation available"

        enrichedReports.add(
            ReportWithRecommendation(
                reportId = report.reportId,
                testName = report.testName,
                testValue = report.testValue,
                unit = report.unit,
                upperLimit = report.upperLimit,
                lowerLimit = report.lowerLimit,
                recommendation = recommendation
            )
        )
    }

    return enrichedReports
}
