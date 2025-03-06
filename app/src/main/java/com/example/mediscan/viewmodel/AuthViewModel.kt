package com.example.mediscan.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mediscan.auth.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<Pair<Boolean, String>>()
    val loginResult: LiveData<Pair<Boolean, String>> get() = _loginResult

    fun signIn(email: String, password: String, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.signIn(email, password)
            _loginResult.postValue(Pair(result.success, result.message))
        }
    }

    fun signUp(email: String, password: String, callback: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val result = authRepository.signUp(email, password)
            callback(result.success, result.message)
        }
    }
}