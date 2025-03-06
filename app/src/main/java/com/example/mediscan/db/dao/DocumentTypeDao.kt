package com.example.mediscan.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mediscan.db.entity.DocumentType

@Dao
interface DocumentTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocumentType(documentType: DocumentType)

    @Query("SELECT * FROM DocumentType")
    suspend fun getAllDocumentTypes(): List<DocumentType>

    @Delete
    suspend fun deleteDocumentType(documentType: DocumentType)
}
