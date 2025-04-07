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

    @Query("UPDATE MedicinePlan SET status = 'Blocked' WHERE planId = :planId")
    suspend fun blockMedicinePlan(planId: Int)

    @Query("UPDATE MedicinePlan SET status = 'Deleted' WHERE planId = :planId")
    suspend fun delteMedicinePlan(planId: Int)

}
