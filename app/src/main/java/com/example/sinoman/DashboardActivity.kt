package com.example.sinoman

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var sectionOneCard: CardView
    private lateinit var registerNowButton: Button
    private lateinit var percentageTextView: TextView
    private lateinit var completionProgressBar: ProgressBar
    private lateinit var completionStatusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize views
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        sectionOneCard = findViewById(R.id.sectionOneCard)
        registerNowButton = findViewById(R.id.registerNowButton)
        percentageTextView = findViewById(R.id.percentageTextView)
        completionProgressBar = findViewById(R.id.completionProgressBar)
        completionStatusTextView = findViewById(R.id.completionStatusTextView)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Set up drawer toggle
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set up navigation listeners
        navigationView.setNavigationItemSelectedListener(this)

        // Set up header click listeners
        val headerView = navigationView.getHeaderView(0)
        headerView.findViewById<View>(R.id.profileImageView).setOnClickListener {
            navigateToProfile()
        }
        headerView.findViewById<View>(R.id.userNameTextView).setOnClickListener {
            navigateToProfile()
        }

        // Set up bottom navigation
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_form -> {
                    navigateToForm()
                    true
                }
                R.id.nav_profile -> {
                    navigateToProfile()
                    true
                }
                R.id.nav_notifications -> {
                    startActivity(Intent(this, NotificationsActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Set up register button
        registerNowButton.setOnClickListener {
            navigateToForm()
        }

        // Update UI based on form data
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val formData = FormData.load(this)
        val completionPercentage = FormData.calculatePersonalDataCompletionPercentage(formData)

        // Update percentage text and progress bar
        percentageTextView.text = "$completionPercentage%"
        completionProgressBar.progress = completionPercentage

        // Update completion status text
        if (completionPercentage < 100) {
            completionStatusTextView.text = "Data mu belum lengkap! Ayo lengkapi sekarang!"
        } else {
            completionStatusTextView.text = "Selamat! Anda telah melengkapi semua data anda!"
        }

        // Hide section 1 if any field is filled
        if (FormData.isAnyFieldFilled(formData)) {
            sectionOneCard.visibility = View.GONE
        } else {
            sectionOneCard.visibility = View.VISIBLE
        }
    }

    private fun navigateToForm() {
        if (FormData.isPersonalDataCompleted(this)) {
            startActivity(Intent(this, FormPage2Activity::class.java))
        } else {
            startActivity(Intent(this, FormActivity::class.java))
        }
    }

    private fun navigateToProfile() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
            }
            R.id.nav_logout -> {
                // Clear user session and navigate to login
                val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                prefs.edit().clear().apply()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}

