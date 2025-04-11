package com.example.sinoman

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.bottomnavigation.BottomNavigationView
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileImageView: CircleImageView
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
    private var originalProfileImageUri: Uri? = null
    private var newProfileImageUri: Uri? = null
    private var isEditing: Boolean = false

    // Activity result launcher for image selection
    private val selectImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data
            if (imageUri != null) {
                // Launch image cropping activity
                val intent = Intent(this, ImageCropActivity::class.java)
                intent.putExtra("imageUri", imageUri)
                cropImageLauncher.launch(intent)
            }
        }
    }

    // Activity result launcher for image cropping
    private val cropImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val croppedImageUri = result.data?.getParcelableExtra<Uri>("croppedImageUri")
            if (croppedImageUri != null) {
                // Update profile image with cropped image
                profileImageView.setImageURI(croppedImageUri)
                newProfileImageUri = croppedImageUri
                checkForChanges()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView)
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
                R.id.nav_help -> {
                    startActivity(Intent(this, HelpActivity::class.java))
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
        val profileImagePath = prefs.getString("profile_image_path", null)

        // Get user data from AuthUtils
        val userData = AuthUtils.getUserData(userEmail)

        // Store original values
        originalUsername = userData.name
        originalAboutMe = aboutMe

        // Load profile image if available
        if (profileImagePath != null) {
            val imageFile = File(profileImagePath)
            if (imageFile.exists()) {
                originalProfileImageUri = Uri.fromFile(imageFile)
                profileImageView.setImageURI(originalProfileImageUri)
            }
        }

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
            openImagePicker()
        }

        // Cancel button
        cancelButton.setOnClickListener {
            // Restore original values
            usernameEditText.setText(originalUsername)
            aboutMeEditText.setText(originalAboutMe)

            // Restore original profile image
            if (originalProfileImageUri != null) {
                profileImageView.setImageURI(originalProfileImageUri)
            } else {
                profileImageView.setImageResource(R.drawable.ic_nav_profile)
            }

            newProfileImageUri = null

            // Hide action buttons
            actionButtonsLayout.visibility = View.GONE
            isEditing = false
        }

        // Save button
        saveButton.setOnClickListener {
            saveProfileChanges()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        selectImageLauncher.launch(intent)
    }

    private fun checkForChanges() {
        val currentUsername = usernameEditText.text.toString()
        val currentAboutMe = aboutMeEditText.text.toString()

        // Check if any field has changed
        val hasChanges = currentUsername != originalUsername ||
                currentAboutMe != originalAboutMe ||
                newProfileImageUri != null

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

        // Save profile image if changed
        var profileImagePath: String? = null
        if (newProfileImageUri != null) {
            // Copy image to app's files directory for persistence
            try {
                val inputStream = contentResolver.openInputStream(newProfileImageUri!!)
                if (inputStream != null) {
                    val outputFile = File(filesDir, "profile_${System.currentTimeMillis()}.jpg")
                    outputFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    profileImagePath = outputFile.absolutePath

                    // Delete old profile image if exists
                    val oldProfileImagePath = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        .getString("profile_image_path", null)
                    if (oldProfileImagePath != null) {
                        val oldFile = File(oldProfileImagePath)
                        if (oldFile.exists() && oldFile.absolutePath != outputFile.absolutePath) {
                            oldFile.delete()
                        }
                    }
                }
                inputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Gagal menyimpan foto profil", Toast.LENGTH_SHORT).show()
            }
        }

        // Save changes to shared preferences
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("user_name", newUsername)
        editor.putString("about_me", newAboutMe)

        // Save profile image path if available
        if (profileImagePath != null) {
            editor.putString("profile_image_path", profileImagePath)
            originalProfileImageUri = Uri.fromFile(File(profileImagePath))
        }

        editor.apply()

        // Update original values
        originalUsername = newUsername
        originalAboutMe = newAboutMe
        newProfileImageUri = null

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
