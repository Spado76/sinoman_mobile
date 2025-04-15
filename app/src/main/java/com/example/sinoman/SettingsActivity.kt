package com.example.sinoman

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout

class SettingsActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var securityContent: View
    private lateinit var notificationsContent: View
    private lateinit var privacyContent: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Set up toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Pengaturan"

        // Initialize views
        tabLayout = findViewById(R.id.tabLayout)
        securityContent = findViewById(R.id.securityContent)
        notificationsContent = findViewById(R.id.notificationsContent)
        privacyContent = findViewById(R.id.privacyContent)

        // Set up tabs
        setupTabs()
        
        // Set default tab (Security)
        showSecurityContent()
        
        // Set up tab selection listener
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> showSecurityContent()
                    1 -> showNotificationsContent()
                    2 -> showPrivacyContent()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        
        // Set up toggle switches
        setupToggleSwitches()
        
        // Set up button click listeners
        setupButtonListeners()
    }

    private fun setupTabs() {
        // Note: We're skipping the "Akun" tab as per requirements
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_security).setText("Keamanan"))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_notifications).setText("Notifikasi"))
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_privacy).setText("Privasi"))
    }

    private fun showSecurityContent() {
        securityContent.visibility = View.VISIBLE
        notificationsContent.visibility = View.GONE
        privacyContent.visibility = View.GONE
    }

    private fun showNotificationsContent() {
        securityContent.visibility = View.GONE
        notificationsContent.visibility = View.VISIBLE
        privacyContent.visibility = View.GONE
    }

    private fun showPrivacyContent() {
        securityContent.visibility = View.GONE
        notificationsContent.visibility = View.GONE
        privacyContent.visibility = View.VISIBLE
    }
    
    private fun setupToggleSwitches() {
        // Implementation for toggle switches would go here
        // This would involve finding the switch views and setting up listeners
    }
    
    private fun setupButtonListeners() {
        // Set up click listeners for "Ubah" buttons
        findViewById<TextView>(R.id.changePasswordButton).setOnClickListener {
            // Handle password change
        }
        
        findViewById<TextView>(R.id.manageSessionsButton).setOnClickListener {
            // Handle session management
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
