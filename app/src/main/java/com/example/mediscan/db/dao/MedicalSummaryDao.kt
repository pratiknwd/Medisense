package com.example.mediscan.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mediscan.db.entity.MedicalSummary
import com.example.mediscan.db.entity.Report

@Dao
interface MedicalSummaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummary(summary: MedicalSummary)

    @Query("SELECT * FROM medical_summaries WHERE userId = :userId AND reportHash = :reportHash LIMIT 1")
    suspend fun getSummaryByUserAndReportHash(userId: Int, reportHash: Int): MedicalSummary?

    @Query("SELECT * FROM medical_summaries WHERE userId = :userId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestSummaryForUser(userId: Int): MedicalSummary?

    @Query("SELECT * FROM medical_summaries WHERE userId = :userId ORDER BY timestamp DESC")
    suspend fun getAllSummariesForUser(userId: Int): List<MedicalSummary>

    @Query(
        """
            SELECT * FROM Report
            WHERE reportTypeId = :reportTypeId
            AND (
                (lowerLimit IS NOT NULL AND testValue < lowerLimit)
                or
                (upperLimit IS NOT NULL AND testValue > upperLimit)
            )
        """
    )
    suspend fun getCriticalReport(reportTypeId: Int): List<Report>
}