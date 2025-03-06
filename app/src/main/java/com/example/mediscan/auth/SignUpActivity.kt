package com.example.mediscan.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mediscan.databinding.ActivitySignUpBinding
import com.example.mediscan.viewmodel.AuthViewModel

typealias SignUpBinding = ActivitySignUpBinding
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: SignUpBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    authViewModel.signUp(email, password) { success, message ->
                        if (success) {
                            Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, SignInActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}