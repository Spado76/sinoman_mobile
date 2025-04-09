package com.example.sinoman

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView
    private lateinit var aboutMeEditText: TextInputEditText
    private lateinit var changePhotoButton: FloatingActionButton
    private lateinit var actionButtonsLayout: LinearLayout
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var bottomNavigation: BottomNavigationView

    // Store original values for cancel functionality
    private var originalUsername: String = ""
    private var originalAboutMe: String = ""
    private var isEditing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        usernameEditText = findViewById(R.id.usernameEditText)
        emailTextView = findViewById(R.id.emailTextView)
        phoneTextView = findViewById(R.id.phoneTextView)
        aboutMeEditText = findViewById(R.id.aboutMeEditText)
        changePhotoButton = findViewById(R.id.changePhotoButton)
        actionButtonsLayout = findViewById(R.id.actionButtonsLayout)
        cancelButton = findViewById(R.id.cancelButton)
        saveButton = findViewById(R.id.saveButton)
        bottomNavigation = findViewById(R.id.bottomNavigation)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up bottom navigation
        bottomNavigation.selectedItemId = R.id.nav_profile
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
                R.id.nav_profile -> true
                else -> false
            }
        }

        // Load user data
        loadUserData()

        // Set up text change listeners to detect edits
        setupTextChangeListeners()

        // Set up button click listeners
        setupButtonListeners()
    }

    private fun loadUserData() {
        // Get user email from shared preferences
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userEmail = prefs.getString("email", "") ?: ""
        val aboutMe = prefs.getString("about_me", "") ?: ""

        // Get user data from AuthUtils
        val userData = AuthUtils.getUserData(userEmail)

        // Store original values
        originalUsername = userData.name
        originalAboutMe = aboutMe

        // Update UI with user data
        usernameEditText.setText(userData.name)
        emailTextView.text = userData.email
        phoneTextView.text = userData.phone
        aboutMeEditText.setText(aboutMe)
    }

    private fun setupTextChangeListeners() {
        // Text watcher to detect changes in editable fields
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkForChanges()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        // Add text watchers to editable fields
        usernameEditText.addTextChangedListener(textWatcher)
        aboutMeEditText.addTextChangedListener(textWatcher)
    }

    private fun setupButtonListeners() {
        // Change photo button
        changePhotoButton.setOnClickListener {
            Toast.makeText(this, "Fitur ganti foto akan segera hadir", Toast.LENGTH_SHORT).show()
        }

        // Cancel button
        cancelButton.setOnClickListener {
            // Restore original values
            usernameEditText.setText(originalUsername)
            aboutMeEditText.setText(originalAboutMe)

            // Hide action buttons
            actionButtonsLayout.visibility = View.GONE
            isEditing = false
        }

        // Save button
        saveButton.setOnClickListener {
            saveProfileChanges()
        }
    }

    private fun checkForChanges() {
        val currentUsername = usernameEditText.text.toString()
        val currentAboutMe = aboutMeEditText.text.toString()

        // Check if any field has changed
        val hasChanges = currentUsername != originalUsername || currentAboutMe != originalAboutMe

        // Show/hide action buttons based on changes
        if (hasChanges && !isEditing) {
            actionButtonsLayout.visibility = View.VISIBLE
            isEditing = true
        } else if (!hasChanges && isEditing) {
            actionButtonsLayout.visibility = View.GONE
            isEditing = false
        }
    }

    private fun saveProfileChanges() {
        val newUsername = usernameEditText.text.toString().trim()
        val newAboutMe = aboutMeEditText.text.toString().trim()

        // Validate input
        if (newUsername.isEmpty()) {
            usernameEditText.error = "Nama pengguna tidak boleh kosong"
            return
        }

        // Save changes to shared preferences
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        prefs.edit()
            .putString("user_name", newUsername)
            .putString("about_me", newAboutMe)
            .apply()

        // Update original values
        originalUsername = newUsername
        originalAboutMe = newAboutMe

        // Hide action buttons
        actionButtonsLayout.visibility = View.GONE
        isEditing = false

        Toast.makeText(this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // If editing, show confirmation dialog
            if (isEditing) {
                showDiscardChangesDialog()
                return true
            }
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDiscardChangesDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Batalkan Perubahan")
            .setMessage("Anda memiliki perubahan yang belum disimpan. Apakah Anda ingin membuang perubahan ini?")
            .setPositiveButton("Ya") { _, _ ->
                // Discard changes and go back
                onBackPressed()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    override fun onBackPressed() {
        // If editing, show confirmation dialog
        if (isEditing) {
            showDiscardChangesDialog()
            return
        }
        super.onBackPressed()
    }
}
