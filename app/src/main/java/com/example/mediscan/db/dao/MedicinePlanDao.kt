package com.example.mediscan.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mediscan.db.entity.MedicinePlan

@Dao
interface MedicinePlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicinePlan(medicinePlan: MedicinePlan): Long

    @Query("SELECT * FROM MedicinePlan WHERE userId = :userId")
    suspend fun getMedicinePlansByUser(userId: Int): List<MedicinePlan>

    @Delete
    suspend fun deleteMedicinePlan(medicinePlan: MedicinePlan)

    @Query("DELETE FROM MedicinePlan WHERE planId = :planId")
    suspend fun deleteMedicinePlanById(planId: Int)
}
