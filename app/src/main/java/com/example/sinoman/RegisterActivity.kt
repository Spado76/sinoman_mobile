package com.example.sinoman

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.Button

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var emailEditText: TextInputEditText
    private lateinit var nameEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var backToLoginButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
        // Initialize views
        emailEditText = findViewById(R.id.emailEditText)
        nameEditText = findViewById(R.id.nameEditText)
        phoneEditText = findViewById(R.id.phoneEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        backToLoginButton = findViewById(R.id.backToLoginButton)
        
        // Set up register button click listener
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val name = nameEditText.text.toString().trim()
            val phone = phoneEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            
            if (email.isEmpty() || name.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Tidak boleh ada bidang yang kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Register user
            if (AuthUtils.registerUser(email, name, phone, password)) {
                Toast.makeText(this, "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()
                finish() // Go back to login screen
            } else {
                Toast.makeText(this, "Pendaftaran gagal!", Toast.LENGTH_SHORT).show()
            }
        }
        
        // Set up back to login button click listener
        backToLoginButton.setOnClickListener {
            finish() // Go back to login screen
        }
    }
}

