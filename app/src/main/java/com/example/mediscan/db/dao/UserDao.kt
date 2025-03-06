package com.example.mediscan.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mediscan.db.entity.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)
    
    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>
}