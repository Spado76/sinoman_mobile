package com.example.sinoman

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class HelpActivity : AppCompatActivity() {

    private lateinit var emailAddressTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var startChatButton: Button
    private lateinit var viewMapButton: Button
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        // Initialize views
        emailAddressTextView = findViewById(R.id.emailAddressTextView)
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView)
        startChatButton = findViewById(R.id.startChatButton)
        viewMapButton = findViewById(R.id.viewMapButton)
        
        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Set up email click listener
        emailAddressTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:bantuan@sinoman.id")
                putExtra(Intent.EXTRA_SUBJECT, "Bantuan Sinoman")
            }
            startActivity(Intent.createChooser(intent, "Kirim Email"))
        }
        
        // Set up phone click listener
        phoneNumberTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:0211234567")
            }
            startActivity(intent)
        }
        
        // Set up WhatsApp chat button
        startChatButton.setOnClickListener {
            val phoneNumber = "+6282137191145"
            val url = "https://wa.me/$phoneNumber"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            startActivity(intent)
        }
        
        // Set up Google Maps button
        viewMapButton.setOnClickListener {
            val mapUrl = "https://maps.app.goo.gl/2gSuoXMRhAdbvNLH9"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(mapUrl)
            }
            startActivity(intent)
        }
        
        // Set up bottom navigation if it exists in the layout
        if (findViewById<BottomNavigationView>(R.id.bottomNavigation) != null) {
            bottomNavigation = findViewById(R.id.bottomNavigation)
            setupBottomNavigation()
        }
    }
    
    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.nav_help
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_notifications -> {
                    startActivity(Intent(this, NotificationsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_form -> {
                    if (FormData.isPersonalDataCompleted(this)) {
                        startActivity(Intent(this, FormPage2Activity::class.java))
                    } else {
                        startActivity(Intent(this, FormActivity::class.java))
                    }
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_help -> true
                else -> false
            }
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
