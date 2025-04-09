package com.example.sinoman

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var userNameTextView: TextView
    private lateinit var userEmailTextView: TextView
    private lateinit var fullNameTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        userNameTextView = findViewById(R.id.userNameTextView)
        userEmailTextView = findViewById(R.id.userEmailTextView)
        fullNameTextView = findViewById(R.id.fullNameTextView)
        phoneTextView = findViewById(R.id.phoneTextView)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up bottom navigation
        bottomNavigation.setSelectedItemId(R.id.nav_profile)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    finish()
                    true
                }
                R.id.nav_form -> {
                    finish()
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }

        // Load user data
        loadUserData()
    }

    private fun loadUserData() {
        // Get user email from shared preferences
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userEmail = prefs.getString("email", "") ?: ""

        // Get user data from AuthUtils
        val userData = AuthUtils.getUserData(userEmail)

        // Update UI with user data
        userEmailTextView.text = userData.email
        userNameTextView.text = userData.name

        // Set phone number from user data
        phoneTextView.text = userData.phone

        // Load form data for profile info
        fullNameTextView.text = userData.name
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

