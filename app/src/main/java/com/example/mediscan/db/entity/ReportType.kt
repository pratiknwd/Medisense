package com.example.mediscan.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ReportType",
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
        )
    ]
)
data class ReportType(
    @PrimaryKey(autoGenerate = true) val reportTypeId: Int,
    val userId: Int,  // Foreign Key
    val documentId: Int,  // Foreign Key
    val reportTypeName: String
)
