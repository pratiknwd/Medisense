package com.example.mediscan.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Report",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Document::class,
            parentColumns = ["documentId"],
            childColumns = ["documentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ReportType::class,
            parentColumns = ["reportTypeId"],
            childColumns = ["reportTypeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Report(
    @PrimaryKey(autoGenerate = true) val reportId: Int,
    val userId: Int,  // Foreign Key
    val documentId: Int,  // Foreign Key
    val reportTypeId: Int,  // Foreign Key
    val testName: String,
    val result: Int,
    val unit: String,
    val upperLimit: Int,
    val lowerLimit: Int
)
