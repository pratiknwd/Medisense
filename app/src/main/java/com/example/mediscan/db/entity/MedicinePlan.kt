package com.example.mediscan.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "MedicinePlan",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MedicinePlan(
    @PrimaryKey(autoGenerate = true)
    val planId: Int = 0,
    val userId: Int,  // Foreign Key to User
    val status: String,  // "Active" or "Blocked"
    val startDate: String,  // Store as String (e.g., "2025-03-07")
    val endDate: String  // Maximum duration from MedicineDetails
)
