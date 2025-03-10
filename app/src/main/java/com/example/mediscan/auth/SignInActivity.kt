package com.example.mediscan.auth


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mediscan.MainActivity
import com.example.mediscan.databinding.ActivitySignInBinding
import com.example.mediscan.viewmodel.AuthViewModel

const val SHARED_PREF_NAME = "SharedPref"
const val PEF_USER_ID = "userId"


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(applicationContext))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        enableEdgeToEdge()
        
        if (isUserLoggedIn(this)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        
        // Observe login result
        authViewModel.loginResult.observe(this) { (success, message) ->
            if (success) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
        
        
        binding.tvSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
    
    
    override fun onResume() {
        super.onResume()
        
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            
            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.signIn(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun isUserLoggedIn(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getInt(PEF_USER_ID, -1,) != -1
    }
    
}
