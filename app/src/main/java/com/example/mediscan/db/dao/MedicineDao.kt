package com.example.mediscan.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.mediscan.medicineplan.MedicinePlanWithDetails

@Dao
interface MedicineDao {
    @Transaction
    @Query("SELECT * FROM MedicinePlan WHERE userId = :userId AND status = 'Active'")
    suspend fun getActiveMedicinePlansForUser(userId: Int): List<MedicinePlanWithDetails>
}
