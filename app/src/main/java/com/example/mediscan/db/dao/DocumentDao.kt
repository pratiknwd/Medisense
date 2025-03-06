package com.example.mediscan.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mediscan.db.entity.Document

@Dao
interface DocumentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(document: Document)

    @Query("SELECT * FROM Document WHERE userId = :userId")
    suspend fun getDocumentsByUserId(userId: Int): List<Document>

    @Query("SELECT * FROM Document WHERE documentTypeId = :documentTypeId")
    suspend fun getDocumentsByType(documentTypeId: Int): List<Document>

    @Delete
    suspend fun deleteDocument(document: Document)
}
