package com.example.mediscan.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mediscan.db.entity.UserFoodTiming

@Dao
interface UserFoodTimingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserFoodTiming(userFoodTiming: UserFoodTiming)

    @Query("SELECT * FROM UserFoodTiming WHERE userId = :userId")
    suspend fun getUserFoodTimingByUserId(userId: Int): UserFoodTiming

    @Delete
    suspend fun deleteUserFoodTiming(userFoodTiming: UserFoodTiming)
}
