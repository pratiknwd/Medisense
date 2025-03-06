package com.example.mediscan.auth

import android.content.Context
import com.example.mediscan.db.AppDatabase
import com.example.mediscan.db.dao.UserDao
import com.example.mediscan.db.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(context: Context) {
    private val userDao: UserDao = AppDatabase.getDatabase(context).userDao()

    suspend fun signUp(email: String, password: String, callback: (Boolean, String) -> Unit) {
        withContext(Dispatchers.IO) {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                callback(false, "User already exists")
            } else {
                val newUser = User(userName = "pratik", userAge = 20, sex = "Male",email = email, password = password)
                userDao.insertUser(newUser)
                callback(true, "Account created successfully")
            }
        }
    }

    suspend fun signIn(email: String, password: String, callback: (Boolean, String) -> Unit) {
        withContext(Dispatchers.IO) {
            val user = userDao.getUserByEmail(email)
            if (user != null && user.password == password) {
                callback(true, "Login successful")
            } else {
                callback(false, "Invalid email or password")
            }
        }
    }
}
