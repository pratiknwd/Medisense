package com.example.mediscan.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

@Entity(
    tableName = "UserFoodTiming",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class UserFoodTiming(
    @PrimaryKey(autoGenerate = true) val itineraryId: Int,
    val userId: Int,  // Foreign Key
    val breakfastTime: String,
    val lunchTime: String,
    val dinnerTime: String
)
