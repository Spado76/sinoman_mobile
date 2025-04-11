package com.example.sinoman

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.sinoman.utils.NotificationManager

class FormPage3bActivity : AppCompatActivity() {

    private lateinit var houseInterestSpinner: Spinner
    private lateinit var provinceSpinner: Spinner
    private lateinit var citySpinner: Spinner
    private lateinit var districtSpinner: Spinner
    private lateinit var villageSpinner: Spinner
    private lateinit var submitButton: Button
    private lateinit var registration: RegistrationData
    private var formChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_page3b)

        // Get registration ID from intent
        val registrationId = intent.getStringExtra("registration_id")
            ?: throw IllegalArgumentException("Registration ID is required")

        // Load registration data
        registration = RegistrationData.getRegistration(this, registrationId)
            ?: throw IllegalArgumentException("Registration not found")

        // Initialize views
        houseInterestSpinner = findViewById(R.id.houseInterestSpinner)
        provinceSpinner = findViewById(R.id.provinceSpinner)
        citySpinner = findViewById(R.id.citySpinner)
        districtSpinner = findViewById(R.id.districtSpinner)
        villageSpinner = findViewById(R.id.villageSpinner)
        submitButton = findViewById(R.id.submitButton)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up spinners
        setupSpinners()

        // Populate form with data
        populateFormWithData()

        // Set up form change listeners
        setupFormChangeListeners()

        // Set up button click listeners
        submitButton.setOnClickListener {
            if (validateForm()) {
                // Disable submit button to prevent multiple submissions
                submitButton.isEnabled = false

                saveFormData()
                registration.status = RegistrationStatus.UNDER_REVIEW
                RegistrationData.addOrUpdateRegistration(this, registration)

                // Update shared preferences to reflect submission status
                val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                prefs.edit()
                    .putBoolean("has_submitted_application", true)
                    .putInt("assistance_type", DashboardActivity.ASSISTANCE_WITHOUT_HOME)
                    .putInt("verification_status", DashboardActivity.STATUS_UNDER_REVIEW)
                    .apply()

                // Add notification for form submission
                NotificationManager.notifyRegistrationSubmitted(this, registration)
                Toast.makeText(this, "Data berhasil dikirim", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setupSpinners() {
        // House Interest Spinner
        val houseInterests = arrayOf(
            "Rumah tapak",
            "Rumah susun",
            "Rumah milik",
            "Rumah sewa",
            "Rumah tipe 21",
            "Rumah tipe 36",
            "Rumah tipe 45",
            "Rumah tipe 54",
            "Rumah tipe 60",
            "Rumah tipe diatas 72",
            "Tanah 0-36 meter persegi",
            "Tanah 36-72 meter persegi",
            "Tanah 72-108 meter persegi",
            "Tanah diatas 108 meter persegi"
        )
        val houseInterestAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, houseInterests)
        houseInterestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        houseInterestSpinner.adapter = houseInterestAdapter

        // Province Spinner - For demo, using a simple list
        val provinces = arrayOf("Jawa Barat", "Jawa Tengah", "Jawa Timur", "DKI Jakarta", "Banten")
        val provinceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        provinceSpinner.adapter = provinceAdapter

        // City Spinner - For demo, using a simple list
        val cities = arrayOf("Kota Bandung", "Kota Bekasi", "Kota Bogor", "Kota Depok", "Kota Cimahi")
        val cityAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities)
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = cityAdapter

        // District Spinner - For demo, using a simple list
        val districts = arrayOf("Kecamatan A", "Kecamatan B", "Kecamatan C", "Kecamatan D", "Kecamatan E")
        val districtAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districts)
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        districtSpinner.adapter = districtAdapter

        // Village Spinner - For demo, using a simple list
        val villages = arrayOf("Desa/Kelurahan 1", "Desa/Kelurahan 2", "Desa/Kelurahan 3", "Desa/Kelurahan 4", "Desa/Kelurahan 5")
        val villageAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, villages)
        villageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        villageSpinner.adapter = villageAdapter
    }

    private fun populateFormWithData() {
        // Set spinner selections based on saved data
        if (registration.houseInterest.isNotBlank()) {
            val houseInterests = resources.getStringArray(R.array.house_interests)
            val position = houseInterests.indexOf(registration.houseInterest)
            if (position >= 0) {
                houseInterestSpinner.setSelection(position)
            }
        }

        if (registration.locationProvince.isNotBlank()) {
            val provinces = resources.getStringArray(R.array.provinces)
            val position = provinces.indexOf(registration.locationProvince)
            if (position >= 0) {
                provinceSpinner.setSelection(position)
            }
        }

        if (registration.locationCity.isNotBlank()) {
            val cities = resources.getStringArray(R.array.cities)
            val position = cities.indexOf(registration.locationCity)
            if (position >= 0) {
                citySpinner.setSelection(position)
            }
        }

        if (registration.locationDistrict.isNotBlank()) {
            val districts = resources.getStringArray(R.array.districts)
            val position = districts.indexOf(registration.locationDistrict)
            if (position >= 0) {
                districtSpinner.setSelection(position)
            }
        }

        if (registration.locationVillage.isNotBlank()) {
            val villages = resources.getStringArray(R.array.villages)
            val position = villages.indexOf(registration.locationVillage)
            if (position >= 0) {
                villageSpinner.setSelection(position)
            }
        }
    }

    private fun setupFormChangeListeners() {
        // Set up spinner listeners
        houseInterestSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        provinceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        districtSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        villageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun validateForm(): Boolean {
        // All fields are required
        if (houseInterestSpinner.selectedItem == null) {
            Toast.makeText(this, "Pilih minat rumah dan tanah", Toast.LENGTH_SHORT).show()
            return false
        }

        if (provinceSpinner.selectedItem == null) {
            Toast.makeText(this, "Pilih provinsi", Toast.LENGTH_SHORT).show()
            return false
        }

        if (citySpinner.selectedItem == null) {
            Toast.makeText(this, "Pilih kabupaten/kota", Toast.LENGTH_SHORT).show()
            return false
        }

        if (districtSpinner.selectedItem == null) {
            Toast.makeText(this, "Pilih kecamatan", Toast.LENGTH_SHORT).show()
            return false
        }

        if (villageSpinner.selectedItem == null) {
            Toast.makeText(this, "Pilih desa/kelurahan", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveFormData() {
        registration.houseInterest = houseInterestSpinner.selectedItem.toString()
        registration.locationProvince = provinceSpinner.selectedItem.toString()
        registration.locationCity = citySpinner.selectedItem.toString()
        registration.locationDistrict = districtSpinner.selectedItem.toString()
        registration.locationVillage = villageSpinner.selectedItem.toString()
        registration.dateUpdated = System.currentTimeMillis()

        RegistrationData.addOrUpdateRegistration(this, registration)
        formChanged = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (formChanged) {
            showSaveDialog()
        } else {
            super.onBackPressed()
        }
    }

    private fun showSaveDialog() {
        AlertDialog.Builder(this)
            .setTitle("Simpan Perubahan")
            .setMessage("Apakah Anda ingin menyimpan perubahan pada form?")
            .setPositiveButton("Ya") { _: DialogInterface, _: Int ->
                saveFormData()
                finish()
            }
            .setNegativeButton("Tidak") { _: DialogInterface, _: Int ->
                finish()
            }
            .setCancelable(true)
            .show()
    }
}

