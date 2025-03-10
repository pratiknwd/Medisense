package com.example.mediscan.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mediscan.db.entity.MedicineDetails

@Dao
interface MedicineDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicineDetails(medicineDetails: MedicineDetails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicineDetailsList(medicineDetails: List<MedicineDetails>)

    @Query("SELECT * FROM MedicineDetails WHERE planId = :planId")
    suspend fun getMedicineDetailsByPlan(planId: Int): List<MedicineDetails>

    @Delete
    suspend fun deleteMedicineDetail(medicineDetails: MedicineDetails)

    @Query("DELETE FROM MedicineDetails WHERE planId = :planId")
    suspend fun deleteMedicineDetailsByPlan(planId: Int)
}
