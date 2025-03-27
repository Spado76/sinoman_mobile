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
import com.google.android.material.textfield.TextInputLayout

class FormPage3aActivity : AppCompatActivity() {

    private lateinit var buildingSizeSpinner: Spinner
    private lateinit var landSizeSpinner: Spinner
    private lateinit var certificateTypeSpinner: Spinner
    private lateinit var financingInterestSpinner: Spinner
    private lateinit var submitButton: Button
    private lateinit var registration: RegistrationData
    private var formChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_page3a)

        // Get registration ID from intent
        val registrationId = intent.getStringExtra("registration_id")
            ?: throw IllegalArgumentException("Registration ID is required")

        // Load registration data
        registration = RegistrationData.getRegistration(this, registrationId)
            ?: throw IllegalArgumentException("Registration not found")

        // Initialize views
        buildingSizeSpinner = findViewById(R.id.buildingSizeSpinner)
        landSizeSpinner = findViewById(R.id.landSizeSpinner)
        certificateTypeSpinner = findViewById(R.id.certificateTypeSpinner)
        financingInterestSpinner = findViewById(R.id.financingInterestSpinner)
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
                saveFormData()
                registration.status = RegistrationStatus.UNDER_REVIEW
                RegistrationData.addOrUpdateRegistration(this, registration)
                Toast.makeText(this, "Data berhasil dikirim", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun setupSpinners() {
        // Building Size Spinner
        val buildingSizes = arrayOf("Tipe 23", "Tipe 54", "Tipe diatas 72")
        val buildingSizeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, buildingSizes)
        buildingSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        buildingSizeSpinner.adapter = buildingSizeAdapter

        // Land Size Spinner
        val landSizes = arrayOf(
            "0-36 meter persegi",
            "36-72 meter persegi",
            "72-108 meter persegi",
            "lebih dari 108 meter persegi"
        )
        val landSizeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, landSizes)
        landSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        landSizeSpinner.adapter = landSizeAdapter

        // Certificate Type Spinner
        val certificateTypes = arrayOf(
            "Hak Milik (SHM)",
            "Hak Guna Bangunan (SHGB)",
            "Hak Satuan Rumah Susun (SHSRS)",
            "Girik atau Petok",
            "Akte Jual Beli (AJB)",
            "Acte van Eigendom"
        )
        val certificateTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, certificateTypes)
        certificateTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        certificateTypeSpinner.adapter = certificateTypeAdapter

        // Financing Interest Spinner
        val financingInterests = arrayOf(
            "Peningkatan luas bangunan",
            "perbaikan atap rumah",
            "perbaikan dinding rumah",
            "perbaikan lantai rumah",
            "penyediaan air minum/bersih",
            "perbaikan/penyediaan sanitasi",
            "perbaikan/penyediaan listrik"
        )
        val financingInterestAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, financingInterests)
        financingInterestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        financingInterestSpinner.adapter = financingInterestAdapter
    }

    private fun populateFormWithData() {
        // Set spinner selections based on saved data
        if (registration.buildingSize.isNotBlank()) {
            val buildingSizes = resources.getStringArray(R.array.building_sizes)
            val position = buildingSizes.indexOf(registration.buildingSize)
            if (position >= 0) {
                buildingSizeSpinner.setSelection(position)
            }
        }

        if (registration.landSize.isNotBlank()) {
            val landSizes = resources.getStringArray(R.array.land_sizes)
            val position = landSizes.indexOf(registration.landSize)
            if (position >= 0) {
                landSizeSpinner.setSelection(position)
            }
        }

        if (registration.certificateType.isNotBlank()) {
            val certificateTypes = resources.getStringArray(R.array.certificate_types)
            val position = certificateTypes.indexOf(registration.certificateType)
            if (position >= 0) {
                certificateTypeSpinner.setSelection(position)
            }
        }

        if (registration.financingInterest.isNotBlank()) {
            val financingInterests = resources.getStringArray(R.array.financing_interests)
            val position = financingInterests.indexOf(registration.financingInterest)
            if (position >= 0) {
                financingInterestSpinner.setSelection(position)
            }
        }
    }

    private fun setupFormChangeListeners() {
        // Set up spinner listeners
        buildingSizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        landSizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        certificateTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        financingInterestSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        if (buildingSizeSpinner.selectedItem == null) {
            Toast.makeText(this, "Pilih luas bangunan/rumah", Toast.LENGTH_SHORT).show()
            return false
        }

        if (landSizeSpinner.selectedItem == null) {
            Toast.makeText(this, "Pilih luas tanah/lahan", Toast.LENGTH_SHORT).show()
            return false
        }

        if (certificateTypeSpinner.selectedItem == null) {
            Toast.makeText(this, "Pilih jenis sertifikat", Toast.LENGTH_SHORT).show()
            return false
        }

        if (financingInterestSpinner.selectedItem == null) {
            Toast.makeText(this, "Pilih minat bantuan pembiayaan", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveFormData() {
        registration.buildingSize = buildingSizeSpinner.selectedItem.toString()
        registration.landSize = landSizeSpinner.selectedItem.toString()
        registration.certificateType = certificateTypeSpinner.selectedItem.toString()
        registration.financingInterest = financingInterestSpinner.selectedItem.toString()
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

