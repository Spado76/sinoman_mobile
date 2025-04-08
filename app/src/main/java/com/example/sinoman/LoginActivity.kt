package com.example.sinoman

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var forgotPasswordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Log.d("LoginActivity", "onCreate called")

        try {
            // Initialize views
            emailEditText = findViewById(R.id.emailEditText)
            passwordEditText = findViewById(R.id.passwordEditText)
            loginButton = findViewById(R.id.loginButton)
            registerButton = findViewById(R.id.registerButton)
            forgotPasswordButton = findViewById(R.id.forgotPasswordButton)

            // Set up login button click listener
            loginButton.setOnClickListener {
                val email = emailEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Mohon isi semua kolom", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Check credentials
                if (AuthUtils.loginUser(email, password)) {
                    // Save user session
                    val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    prefs.edit().putString("email", email).apply()

                    try {
                        // Navigate to dashboard with explicit intent
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        Log.d("LoginActivity", "Starting DashboardActivity")
                        startActivity(intent)
                        finish() // Close LoginActivity
                    } catch (e: Exception) {
                        Log.e("LoginActivity", "Error starting DashboardActivity", e)
                        Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Email atau kata sandi tidak valid", Toast.LENGTH_SHORT).show()
                }
            }

            // Set up register button click listener
            registerButton.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            // Set up forgot password button click listener
            forgotPasswordButton.setOnClickListener {
                Toast.makeText(this, "Fitur lupa kata sandi akan segera hadir", Toast.LENGTH_SHORT).show()
                // Functionality will be implemented later
            }
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error in onCreate", e)
        }
    }
}

