package com.example.sinoman

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.sinoman.model.Notification
import com.example.sinoman.model.NotificationType
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var bottomNavigation: BottomNavigationView

    // Dashboard UI elements
    private lateinit var welcomeTextView: TextView
    private lateinit var queuePositionTextView: TextView
    private lateinit var verificationStatusTextView: TextView
    private lateinit var statusBgView: View
    private lateinit var statusIconView: ImageView
    private lateinit var locationTextView: TextView
    private lateinit var assistanceTypeTextView: TextView
    private lateinit var progressPercentageTextView: TextView
    private lateinit var registrationProgressBar: ProgressBar

    // Progress indicators
    private lateinit var profileCheckIcon: ImageView
    private lateinit var applicationCheckIcon: ImageView
    private lateinit var verificationCheckIcon: ImageView
    private lateinit var allocationCheckIcon: ImageView

    // Recent Activities
    private lateinit var recentActivitiesContainer: LinearLayout
    private lateinit var viewAllActivitiesButton: TextView

    // Verification status constants
    companion object {
        const val STATUS_NOT_REGISTERED = 0
        const val STATUS_UNDER_REVIEW = 1
        const val STATUS_VERIFIED = 2

        const val ASSISTANCE_NONE = 0
        const val ASSISTANCE_WITH_HOME = 1
        const val ASSISTANCE_WITHOUT_HOME = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Initialize views
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Initialize dashboard UI elements
        welcomeTextView = findViewById(R.id.welcomeTextView)
        queuePositionTextView = findViewById(R.id.queuePositionTextView)
        verificationStatusTextView = findViewById(R.id.verificationStatusTextView)
        statusBgView = findViewById(R.id.statusBgView)
        statusIconView = findViewById(R.id.statusIconView)
        locationTextView = findViewById(R.id.locationTextView)
        assistanceTypeTextView = findViewById(R.id.assistanceTypeTextView)
        progressPercentageTextView = findViewById(R.id.progressPercentageTextView)
        registrationProgressBar = findViewById(R.id.registrationProgressBar)

        // Initialize progress indicators
        profileCheckIcon = findViewById(R.id.profileCheckIcon)
        applicationCheckIcon = findViewById(R.id.applicationCheckIcon)
        verificationCheckIcon = findViewById(R.id.verificationCheckIcon)
        allocationCheckIcon = findViewById(R.id.allocationCheckIcon)

        // Initialize recent activities section
        recentActivitiesContainer = findViewById(R.id.recentActivitiesContainer)
        viewAllActivitiesButton = findViewById(R.id.viewAllActivitiesButton)

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

        // Set up view all activities button
        viewAllActivitiesButton.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        // Update UI based on form data
        updateUI()

        // Load recent activities
        loadRecentActivities()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
        loadRecentActivities() // Refresh activities when returning to the dashboard
    }

    private fun updateUI() {
        val formData = FormData.load(this)
        val completionPercentage = FormData.calculatePersonalDataCompletionPercentage(formData)

        // Get user name from shared preferences
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userName = prefs.getString("user_name", "User") ?: "User"

        // Update welcome message
        welcomeTextView.text = "Selamat datang kembali, $userName"

        // Update queue position (static for now)
        queuePositionTextView.text = "45"

        // Update location (static for now)
        locationTextView.text = "Jakarta Timur"

        // Calculate progress percentage
        var progressSteps = 0
        var totalSteps = 3 // Profile, Application, Verification (Allocation is not counted in progress)

        // Check profile completion
        val isProfileComplete = FormData.isPersonalDataCompleted(this)
        if (isProfileComplete) {
            profileCheckIcon.visibility = View.VISIBLE
            progressSteps++
        } else {
            profileCheckIcon.visibility = View.GONE
        }

        // Determine application status and assistance type
        val hasSubmittedApplication = prefs.getBoolean("has_submitted_application", false)
        val assistanceType = prefs.getInt("assistance_type", ASSISTANCE_NONE)

        // Update assistance type text
        when (assistanceType) {
            ASSISTANCE_WITH_HOME -> assistanceTypeTextView.text = "Bantuan Punya Rumah"
            ASSISTANCE_WITHOUT_HOME -> assistanceTypeTextView.text = "Bantuan Tidak Punya Rumah"
            else -> assistanceTypeTextView.text = "Belum Dipilih"
        }

        // Update application status
        if (hasSubmittedApplication) {
            applicationCheckIcon.visibility = View.VISIBLE
            progressSteps++
        } else {
            applicationCheckIcon.visibility = View.GONE
        }

        // Determine verification status
        val verificationStatus = prefs.getInt("verification_status", STATUS_NOT_REGISTERED)

        // Update verification status UI
        when (verificationStatus) {
            STATUS_NOT_REGISTERED -> {
                verificationStatusTextView.text = "Belum Mendaftar"
                statusBgView.setBackgroundResource(R.drawable.bg_circle_yellow)
                statusIconView.setImageResource(R.drawable.ic_time)
                statusIconView.setColorFilter(resources.getColor(R.color.warning_orange))
                verificationCheckIcon.visibility = View.GONE
            }
            STATUS_UNDER_REVIEW -> {
                verificationStatusTextView.text = "Sedang Ditinjau"
                statusBgView.setBackgroundResource(R.drawable.bg_circle_yellow)
                statusIconView.setImageResource(R.drawable.ic_pending)
                statusIconView.setColorFilter(resources.getColor(R.color.warning_orange))
                verificationCheckIcon.visibility = View.VISIBLE
                verificationCheckIcon.setImageResource(R.drawable.ic_pending)
            }
            STATUS_VERIFIED -> {
                verificationStatusTextView.text = "Terverifikasi"
                statusBgView.setBackgroundResource(R.drawable.bg_circle_green)
                statusIconView.setImageResource(R.drawable.ic_check_circle)
                statusIconView.setColorFilter(resources.getColor(R.color.success_green))
                verificationCheckIcon.visibility = View.VISIBLE
                verificationCheckIcon.setImageResource(R.drawable.ic_check_small)
                progressSteps++
            }
        }

        // Calculate and update progress percentage
        val progressPercentage = (progressSteps * 100) / totalSteps
        progressPercentageTextView.text = "$progressPercentage%"
        registrationProgressBar.progress = progressPercentage
    }

    private fun loadRecentActivities() {
        // Clear existing activities
        recentActivitiesContainer.removeAllViews()

        // Get recent notifications
        val notifications = Notification.loadNotifications(this)

        if (notifications.isEmpty()) {
            // If no notifications, add a placeholder
            val inflater = LayoutInflater.from(this)
            val view = inflater.inflate(R.layout.item_recent_activity, recentActivitiesContainer, false)

            val titleTextView = view.findViewById<TextView>(R.id.activityTitleTextView)
            val timeTextView = view.findViewById<TextView>(R.id.activityTimeTextView)
            val iconBackground = view.findViewById<View>(R.id.iconBackground)
            val iconImageView = view.findViewById<ImageView>(R.id.iconImageView)

            titleTextView.text = "Belum ada aktivitas terbaru"
            timeTextView.visibility = View.GONE
            iconBackground.setBackgroundResource(R.drawable.bg_circle_blue_light)
            iconImageView.setImageResource(R.drawable.ic_document_blue)

            recentActivitiesContainer.addView(view)
            return
        }

        // Take the 3 most recent notifications
        val recentNotifications = notifications.take(3)

        // Add each notification to the container
        for (notification in recentNotifications) {
            addActivityItem(notification)
        }
    }

    private fun addActivityItem(notification: Notification) {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.item_recent_activity, recentActivitiesContainer, false)

        val titleTextView = view.findViewById<TextView>(R.id.activityTitleTextView)
        val timeTextView = view.findViewById<TextView>(R.id.activityTimeTextView)
        val iconBackground = view.findViewById<View>(R.id.iconBackground)
        val iconImageView = view.findViewById<ImageView>(R.id.iconImageView)

        // Set notification title
        titleTextView.text = notification.title

        // Set time ago text
        timeTextView.text = getTimeAgoString(notification.timestamp)

        // Set icon based on notification type
        when (notification.type) {
            NotificationType.DATA_COMPLETION -> {
                iconBackground.setBackgroundResource(R.drawable.bg_circle_blue_light)
                iconImageView.setImageResource(R.drawable.ic_document_blue)
            }
            NotificationType.REGISTRATION_SUBMISSION -> {
                iconBackground.setBackgroundResource(R.drawable.bg_circle_green_light)
                iconImageView.setImageResource(R.drawable.ic_arrow_up_green)
            }
            NotificationType.SYSTEM -> {
                iconBackground.setBackgroundResource(R.drawable.bg_circle_yellow_light)
                iconImageView.setImageResource(R.drawable.ic_warning_yellow)
            }
        }

        recentActivitiesContainer.addView(view)
    }

    private fun getTimeAgoString(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < TimeUnit.MINUTES.toMillis(1) -> "Baru saja"
            diff < TimeUnit.HOURS.toMillis(1) -> {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
                "$minutes menit yang lalu"
            }
            diff < TimeUnit.DAYS.toMillis(1) -> {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                "$hours jam yang lalu"
            }
            diff < TimeUnit.DAYS.toMillis(7) -> {
                val days = TimeUnit.MILLISECONDS.toDays(diff)
                "$days hari yang lalu"
            }
            diff < TimeUnit.DAYS.toMillis(30) -> {
                val weeks = TimeUnit.MILLISECONDS.toDays(diff) / 7
                "$weeks minggu yang lalu"
            }
            else -> {
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
                sdf.format(Date(timestamp))
            }
        }
    }

    private fun navigateToForm() {
        if (FormData.isPersonalDataCompleted(this)) {
            // If personal data is complete, show application form options
            showApplicationOptions()
        } else {
            startActivity(Intent(this, FormActivity::class.java))
        }
    }

    private fun showApplicationOptions() {
        // This would typically show a dialog with options for the two types of applications
        // For now, we'll simulate submitting an application
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val hasSubmitted = prefs.getBoolean("has_submitted_application", false)

        if (!hasSubmitted) {
            // Simulate submitting an application for housing assistance
            prefs.edit()
                .putBoolean("has_submitted_application", true)
                .putInt("assistance_type", ASSISTANCE_WITH_HOME)
                .putInt("verification_status", STATUS_UNDER_REVIEW)
                .apply()

            // Add a notification for the submission
            val notification = Notification(
                title = "Dokumen KK Anda telah diverifikasi",
                message = "Dokumen Kartu Keluarga Anda telah berhasil diverifikasi oleh sistem.",
                type = NotificationType.DATA_COMPLETION
            )
            Notification.addNotification(this, notification)

            // Update UI to reflect changes
            updateUI()
            loadRecentActivities()
        } else {
            // If already submitted, show form details or status
            startActivity(Intent(this, FormPage2Activity::class.java))
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

