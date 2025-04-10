package com.example.sinoman

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var nextButton: Button
    private lateinit var guideButton: Button
    private lateinit var aboutButton: Button
    private lateinit var termsButton: Button
    private lateinit var faqButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is already logged in
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = prefs.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            // Skip login, go directly to dashboard
            navigateToDashboard()
            return
        }

        setContentView(R.layout.activity_main)

        // Initialize buttons
        nextButton = findViewById(R.id.nextButton)
        guideButton = findViewById(R.id.guideButton)
        aboutButton = findViewById(R.id.aboutButton)
        termsButton = findViewById(R.id.termsButton)
        faqButton = findViewById(R.id.faqButton)

        // Set click listeners
        nextButton.setOnClickListener {
            // Navigate to login screen
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        aboutButton.setOnClickListener {
            // Navigate to about screen
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        // Other button click listeners can be implemented similarly
        guideButton.setOnClickListener {
            val intent = Intent(this, UserGuideActivity::class.java)
            startActivity(intent)
        }

        termsButton.setOnClickListener {
            // TODO: Implement terms and conditions functionality
        }

        faqButton.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }
    }
    private fun navigateToDashboard() {
        try {
            // Navigate to dashboard with explicit intent
            val intent = Intent(this, DashboardActivity::class.java)
            Log.d("LoginActivity", "Starting DashboardActivity")
            startActivity(intent)
            finish() // Close LoginActivity
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error starting DashboardActivity", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
