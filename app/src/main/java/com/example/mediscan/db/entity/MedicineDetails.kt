package com.example.mediscan.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "MedicineDetails",
    foreignKeys = [
        ForeignKey(
            entity = MedicinePlan::class,
            parentColumns = ["planId"],
            childColumns = ["planId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MedicineDetails(
    @PrimaryKey(autoGenerate = true) val medicineId: Int = 0,
    val planId: Int,  // Foreign Key to MedicinePlan
    val medicineName: String,
    val dose: String,
    val frequency: String,
    val duration: Int, // Number of days
    val times: String,
    val foodInstruction: String
)
