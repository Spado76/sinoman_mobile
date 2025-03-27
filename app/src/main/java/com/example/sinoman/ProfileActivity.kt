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
    private lateinit var addressTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        userNameTextView = findViewById(R.id.userNameTextView)
        userEmailTextView = findViewById(R.id.userEmailTextView)
        fullNameTextView = findViewById(R.id.fullNameTextView)
        addressTextView = findViewById(R.id.addressTextView)
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
        // In a real app, you would load this from a user profile database
        // For this example, we'll use hardcoded values and form data
        val userEmail = "admin@gmail.com" // From login
        userEmailTextView.text = userEmail
        userNameTextView.text = "Admin User"

        // Load form data for profile info
        val formData = FormData.load(this)
        if (formData.name.isNotBlank()) {
            fullNameTextView.text = formData.name
        } else {
            fullNameTextView.text = "Not provided"
        }

        if (formData.address.isNotBlank()) {
            addressTextView.text = formData.address
        } else {
            addressTextView.text = "Not provided"
        }

        if (formData.phone.isNotBlank()) {
            phoneTextView.text = formData.phone
        } else {
            phoneTextView.text = "Not provided"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

