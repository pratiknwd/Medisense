package com.example.mediscan.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "Document",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DocumentType::class,
            parentColumns = ["documentTypeId"],
            childColumns = ["documentTypeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Document(
    @PrimaryKey(autoGenerate = true) val documentId: Int,
    val userId: Int,  // Foreign Key
    val documentTypeId: Int,  // Foreign Key
    val documentLink: String
)
