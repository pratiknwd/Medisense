package com.example.mediscan.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mediscan.db.entity.Report

@Dao
interface ReportDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: Report)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllReports(reports: List<Report>)
    
    @Query("SELECT * FROM Report WHERE userId = :userId")
    suspend fun getReportsByUser(userId: Int): List<Report>
    
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
    
    @Query("SELECT * FROM Report WHERE reportTypeId = :reportTypeId")
    suspend fun getReportsByType(reportTypeId: Int): List<Report>
    
    @Delete
    suspend fun deleteReport(report: Report)
}
