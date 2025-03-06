package com.example.mediscan.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mediscan.db.dao.DocumentDao
import com.example.mediscan.db.dao.DocumentTypeDao
import com.example.mediscan.db.dao.MedicinePlanDao
import com.example.mediscan.db.dao.ReportDao
import com.example.mediscan.db.dao.ReportTypeDao
import com.example.mediscan.db.dao.UserDao
import com.example.mediscan.db.dao.UserFoodTimingDao
import com.example.mediscan.db.entity.Document
import com.example.mediscan.db.entity.DocumentType
import com.example.mediscan.db.entity.MedicinePlan
import com.example.mediscan.db.entity.Report
import com.example.mediscan.db.entity.ReportType
import com.example.mediscan.db.entity.User
import com.example.mediscan.db.entity.UserFoodTiming

@Database(
    entities = [User::class, MedicinePlan::class, UserFoodTiming::class, Document::class, DocumentType::class, Report::class, ReportType::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun medicinePlanDao(): MedicinePlanDao
    abstract fun userFoodTimingDao(): UserFoodTimingDao
    abstract fun documentDao(): DocumentDao
    abstract fun documentTypeDao(): DocumentTypeDao
    abstract fun reportDao(): ReportDao
    abstract fun reportTypeDao(): ReportTypeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}