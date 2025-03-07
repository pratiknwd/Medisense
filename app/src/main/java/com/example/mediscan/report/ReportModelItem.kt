package com.example.mediscan.report


import com.example.mediscan.db.entity.Report
import com.google.gson.annotations.SerializedName

data class ReportModelItem(
    @SerializedName("bio reference interval")
    val bioReferenceInterval: String,
    val explanation: String,
    @SerializedName("maximum_value")
    val maximumValue: String,
    @SerializedName("minimum_value")
    val minimumValue: String,
    @SerializedName("test_name")
    val testName: String,
    @SerializedName("test_value")
    val testValue: String,
    val units: String,
) {
    
    override fun toString(): String {
        return """
            ReportModelItem(
                bioReferenceInterval="$bioReferenceInterval",
                explanation="$explanation",
                maximumValue=$maximumValue,
                minimumValue=$minimumValue,
                testName="$testName",
                testValue=$testValue,
                units="$units"
            )
        """.trimIndent()
    }
    
    fun toEntity(userId: Int, reportTypeId: Int): Report {
        return Report(
            reportId = 0, // Assuming auto-generation of ID
            userId = userId,
            reportTypeId = reportTypeId,
            testName = testName, // Assign if not blank, else null
            testValue = try { testValue.toFloat() } catch (e: Exception) { null }, // Convert safely
            unit = units , // Assign if not blank
            upperLimit = try { maximumValue.toFloat() } catch (e: Exception) { 10000F }, // Convert safely
            lowerLimit = try { minimumValue.toFloat() } catch (e: Exception) { -10000F }, // Convert safely
            explanation = explanation, // Assign if not blank
            bioReferenceInterval = bioReferenceInterval // Assign if not blank
        )
    }
}