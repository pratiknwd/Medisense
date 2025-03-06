package com.example.mediscan.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mediscan.db.entity.ReportType

@Dao
interface ReportTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportType(reportType: ReportType)

    @Query("SELECT * FROM ReportType WHERE userId = :userId")
    suspend fun getReportTypesByUser(userId: Int): List<ReportType>

    @Delete
    suspend fun deleteReportType(reportType: ReportType)
}
