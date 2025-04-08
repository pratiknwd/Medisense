package com.example.mediscan.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "medical_summaries",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class MedicalSummary(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int, // Foreign key to the User table
    val summaryText: String,
    val reportHash: Int, // Still useful to identify the set of reports summarized
    val timestamp: Long = System.currentTimeMillis()
)