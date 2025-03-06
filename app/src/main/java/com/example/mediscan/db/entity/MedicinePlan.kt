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
    @PrimaryKey(autoGenerate = true) val planId: Int,
    val userId: Int,  // Foreign Key
    val status: Boolean,
    val medicineName: String,
    val dose: String,
    val frequency: String,
    val duration: String,
    val times: String,
    val foodInstruction: String,
    val startDate: String
)
