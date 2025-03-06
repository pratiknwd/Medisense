package com.example.mediscan.auth

import android.content.Context
import com.example.mediscan.db.AppDatabase
import com.example.mediscan.db.dao.UserDao
import com.example.mediscan.db.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class AuthResult(val success: Boolean, val message: String)

class AuthRepository(context: Context) {
    private val userDao: UserDao = AppDatabase.getDatabase(context).userDao()
    
    suspend fun signUp(username: String, age: Int, gender: String, email: String, password: String): AuthResult {
        return withContext(Dispatchers.IO) {
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                AuthResult(false, "User already exists")
            } else {
                val newUser = User(userName = username, userAge = age, sex = gender, email = email, password = password)
                userDao.insertUser(newUser)
                AuthResult(true, "Account created successfully")
            }
        }
    }
    
    suspend fun signIn(email: String, password: String): AuthResult {
        return withContext(Dispatchers.IO) {
            val user = userDao.getUserByEmail(email)
            if (user != null && user.password == password) {
                AuthResult(true, "Login successful")
            } else {
                AuthResult(false, "Invalid email or password")
            }
        }
    }
}

