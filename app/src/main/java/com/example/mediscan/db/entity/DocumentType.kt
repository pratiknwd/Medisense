package com.example.mediscan.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DocumentType")
data class DocumentType(
    @PrimaryKey(autoGenerate = true) val documentTypeId: Int,
    val documentName: String
)
