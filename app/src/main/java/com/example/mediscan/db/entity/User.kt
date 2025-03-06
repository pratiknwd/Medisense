package com.example.mediscan.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int,
    val userName: String,
    val userAge: Int,
    val sex: String,
    val email: String,
    val password: String
)
