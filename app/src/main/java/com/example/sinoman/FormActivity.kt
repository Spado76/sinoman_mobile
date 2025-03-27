package com.example.sinoman

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FormActivity : AppCompatActivity() {

    private lateinit var nameEditText: TextInputEditText
    private lateinit var nikEditText: TextInputEditText
    private lateinit var nikPhotoButton: Button
    private lateinit var nikPhotoNameTextView: TextView
    private lateinit var npwpEditText: TextInputEditText
    private lateinit var npwpPhotoButton: Button
    private lateinit var npwpPhotoNameTextView: TextView
    private lateinit var familyCardEditText: TextInputEditText
    private lateinit var familyCardPhotoButton: Button
    private lateinit var familyCardPhotoNameTextView: TextView
    private lateinit var currentAddressEditText: TextInputEditText
    private lateinit var hometownAddressEditText: TextInputEditText
    private lateinit var genderSpinner: Spinner
    private lateinit var maritalStatusSpinner: Spinner
    private lateinit var childrenSpinner: Spinner
    private lateinit var educationSpinner: Spinner
    private lateinit var occupationSpinner: Spinner
    private lateinit var incomeSpinner: Spinner
    private lateinit var allowanceSpinner: Spinner
    private lateinit var expensesSpinner: Spinner
    private lateinit var nextButton: Button

    private var formData = FormData()
    private var formChanged = false
    private var currentPhotoType = ""

    // Image picker launcher
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val imagePath = saveImageToInternalStorage(uri)
            when (currentPhotoType) {
                "nik" -> {
                    formData.nikPhotoPath = imagePath
                    nikPhotoNameTextView.text = "Foto dipilih"
                }
                "npwp" -> {
                    formData.npwpPhotoPath = imagePath
                    npwpPhotoNameTextView.text = "Foto dipilih"
                }
                "family_card" -> {
                    formData.familyCardPhotoPath = imagePath
                    familyCardPhotoNameTextView.text = "Foto dipilih"
                }
            }
            formChanged = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        // Initialize views
        initializeViews()

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up spinners
        setupSpinners()

        // Load saved form data
        formData = FormData.load(this)
        populateFormWithData()

        // Set up form change listeners
        setupFormChangeListeners()

        // Set up photo buttons
        setupPhotoButtons()

        // Set up next button
        nextButton.setOnClickListener {
            saveFormData()
            val intent = Intent(this, FormPage2Activity::class.java)
            startActivity(intent)
        }
    }

    private fun initializeViews() {
        nameEditText = findViewById(R.id.nameEditText)
        nikEditText = findViewById(R.id.nikEditText)
        nikPhotoButton = findViewById(R.id.nikPhotoButton)
        nikPhotoNameTextView = findViewById(R.id.nikPhotoNameTextView)
        npwpEditText = findViewById(R.id.npwpEditText)
        npwpPhotoButton = findViewById(R.id.npwpPhotoButton)
        npwpPhotoNameTextView = findViewById(R.id.npwpPhotoNameTextView)
        familyCardEditText = findViewById(R.id.familyCardEditText)
        familyCardPhotoButton = findViewById(R.id.familyCardPhotoButton)
        familyCardPhotoNameTextView = findViewById(R.id.familyCardPhotoNameTextView)
        currentAddressEditText = findViewById(R.id.currentAddressEditText)
        hometownAddressEditText = findViewById(R.id.hometownAddressEditText)
        genderSpinner = findViewById(R.id.genderSpinner)
        maritalStatusSpinner = findViewById(R.id.maritalStatusSpinner)
        childrenSpinner = findViewById(R.id.childrenSpinner)
        educationSpinner = findViewById(R.id.educationSpinner)
        occupationSpinner = findViewById(R.id.occupationSpinner)
        incomeSpinner = findViewById(R.id.incomeSpinner)
        allowanceSpinner = findViewById(R.id.allowanceSpinner)
        expensesSpinner = findViewById(R.id.expensesSpinner)
        nextButton = findViewById(R.id.nextButton)
    }

    private fun setupSpinners() {
        // Gender Spinner
        val genders = arrayOf("Laki-laki", "Perempuan")
        val genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genders)
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = genderAdapter

        // Marital Status Spinner
        val maritalStatuses = arrayOf("Menikah", "Belum menikah", "Cerai hidup", "Cerai mati")
        val maritalStatusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, maritalStatuses)
        maritalStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        maritalStatusSpinner.adapter = maritalStatusAdapter

        // Children Spinner
        val childrenOptions = arrayOf("1 anak", "2 anak", "3 anak", "4 anak atau lebih")
        val childrenAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, childrenOptions)
        childrenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        childrenSpinner.adapter = childrenAdapter

        // Education Spinner
        val educationOptions = arrayOf("Diploma 1/2/3", "Strata 1", "Strata 2", "Strata 3")
        val educationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, educationOptions)
        educationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        educationSpinner.adapter = educationAdapter

        // Occupation Spinner
        val occupations = arrayOf(
            "TNI",
            "Polri",
            "ASN",
            "Pegawai BUMN/Swasta",
            "Pedagang",
            "Peternak",
            "Perajin",
            "Petani",
            "Buruh bangunan",
            "Tenaga kebersihan"
        )
        val occupationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, occupations)
        occupationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        occupationSpinner.adapter = occupationAdapter

        // Income Spinner
        val incomeRanges = arrayOf(
            "0-1.2 juta",
            "1.2-1.8 juta",
            "1.8-2.1 juta",
            "2.1-2.6 juta",
            "2.6-3.1 juta",
            "3.1-3.6 juta",
            "3.6-4.2 juta",
            "4.2-5.2 juta",
            "5.2-7 juta",
            "7-13.9 juta"
        )
        val incomeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, incomeRanges)
        incomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        incomeSpinner.adapter = incomeAdapter

        // Allowance Spinner
        val allowanceRanges = arrayOf(
            "0-1.2 juta",
            "1.2-1.8 juta",
            "1.8-2.1 juta",
            "2.1-2.6 juta",
            "2.6-3.1 juta",
            "3.1-3.6 juta",
            "3.6-4.2 juta",
            "4.2-5.2 juta",
            "5.2-7 juta",
            "lebih dari 7 juta"
        )
        val allowanceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, allowanceRanges)
        allowanceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        allowanceSpinner.adapter = allowanceAdapter

        // Expenses Spinner
        val expensesRanges = arrayOf(
            "0-1.2 juta",
            "1.2-1.4 juta",
            "1.4-1.6 juta",
            "1.6-1.8 juta",
            "1.8-2.1 juta",
            "2.1-2.3 juta",
            "2.3-2.7 juta",
            "2.7-3.2 juta",
            "3.2-4.3 juta",
            "lebih dari 4.3 juta"
        )
        val expensesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, expensesRanges)
        expensesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        expensesSpinner.adapter = expensesAdapter
    }

    private fun setupPhotoButtons() {
        nikPhotoButton.setOnClickListener {
            currentPhotoType = "nik"
            getContent.launch("image/*")
        }

        npwpPhotoButton.setOnClickListener {
            currentPhotoType = "npwp"
            getContent.launch("image/*")
        }

        familyCardPhotoButton.setOnClickListener {
            currentPhotoType = "family_card"
            getContent.launch("image/*")
        }
    }

    private fun populateFormWithData() {
        nameEditText.setText(formData.name)
        nikEditText.setText(formData.nik)
        npwpEditText.setText(formData.npwp)
        familyCardEditText.setText(formData.familyCardNumber)
        currentAddressEditText.setText(formData.currentAddress)
        hometownAddressEditText.setText(formData.hometownAddress)

        // Update photo text views
        if (formData.nikPhotoPath.isNotBlank()) {
            nikPhotoNameTextView.text = "Foto dipilih"
        }
        if (formData.npwpPhotoPath.isNotBlank()) {
            npwpPhotoNameTextView.text = "Foto dipilih"
        }
        if (formData.familyCardPhotoPath.isNotBlank()) {
            familyCardPhotoNameTextView.text = "Foto dipilih"
        }

        // Set spinner selections
        if (formData.gender.isNotBlank()) {
            val genders = resources.getStringArray(R.array.genders)
            val position = genders.indexOf(formData.gender)
            if (position >= 0) {
                genderSpinner.setSelection(position)
            }
        }

        if (formData.maritalStatus.isNotBlank()) {
            val maritalStatuses = resources.getStringArray(R.array.marital_statuses)
            val position = maritalStatuses.indexOf(formData.maritalStatus)
            if (position >= 0) {
                maritalStatusSpinner.setSelection(position)
            }
        }

        if (formData.numberOfChildren.isNotBlank()) {
            val childrenOptions = resources.getStringArray(R.array.children_options)
            val position = childrenOptions.indexOf(formData.numberOfChildren)
            if (position >= 0) {
                childrenSpinner.setSelection(position)
            }
        }

        if (formData.education.isNotBlank()) {
            val educationOptions = resources.getStringArray(R.array.education_options)
            val position = educationOptions.indexOf(formData.education)
            if (position >= 0) {
                educationSpinner.setSelection(position)
            }
        }

        if (formData.occupation.isNotBlank()) {
            val occupations = resources.getStringArray(R.array.occupations)
            val position = occupations.indexOf(formData.occupation)
            if (position >= 0) {
                occupationSpinner.setSelection(position)
            }
        }

        if (formData.monthlyIncome.isNotBlank()) {
            val incomeRanges = resources.getStringArray(R.array.income_ranges)
            val position = incomeRanges.indexOf(formData.monthlyIncome)
            if (position >= 0) {
                incomeSpinner.setSelection(position)
            }
        }

        if (formData.monthlyAllowance.isNotBlank()) {
            val allowanceRanges = resources.getStringArray(R.array.allowance_ranges)
            val position = allowanceRanges.indexOf(formData.monthlyAllowance)
            if (position >= 0) {
                allowanceSpinner.setSelection(position)
            }
        }

        if (formData.monthlyExpenses.isNotBlank()) {
            val expensesRanges = resources.getStringArray(R.array.expenses_ranges)
            val position = expensesRanges.indexOf(formData.monthlyExpenses)
            if (position >= 0) {
                expensesSpinner.setSelection(position)
            }
        }
    }

    private fun setupFormChangeListeners() {
        nameEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && nameEditText.text.toString() != formData.name) {
                formChanged = true
            }
        }

        nikEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && nikEditText.text.toString() != formData.nik) {
                formChanged = true
            }
        }

        npwpEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && npwpEditText.text.toString() != formData.npwp) {
                formChanged = true
            }
        }

        familyCardEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && familyCardEditText.text.toString() != formData.familyCardNumber) {
                formChanged = true
            }
        }

        currentAddressEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && currentAddressEditText.text.toString() != formData.currentAddress) {
                formChanged = true
            }
        }

        hometownAddressEditText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && hometownAddressEditText.text.toString() != formData.hometownAddress) {
                formChanged = true
            }
        }

        // Set up spinner listeners
        genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        maritalStatusSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        childrenSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        educationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        occupationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        incomeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        allowanceSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        expensesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                formChanged = true
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun saveFormData() {
        formData.name = nameEditText.text.toString().trim()
        formData.nik = nikEditText.text.toString().trim()
        formData.npwp = npwpEditText.text.toString().trim()
        formData.familyCardNumber = familyCardEditText.text.toString().trim()
        formData.currentAddress = currentAddressEditText.text.toString().trim()
        formData.hometownAddress = hometownAddressEditText.text.toString().trim()
        formData.gender = genderSpinner.selectedItem.toString()
        formData.maritalStatus = maritalStatusSpinner.selectedItem.toString()
        formData.numberOfChildren = childrenSpinner.selectedItem.toString()
        formData.education = educationSpinner.selectedItem.toString()
        formData.occupation = occupationSpinner.selectedItem.toString()
        formData.monthlyIncome = incomeSpinner.selectedItem.toString()
        formData.monthlyAllowance = allowanceSpinner.selectedItem.toString()
        formData.monthlyExpenses = expensesSpinner.selectedItem.toString()

        FormData.save(this, formData)
        formChanged = false
    }

    private fun saveImageToInternalStorage(uri: Uri): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_${timeStamp}_${currentPhotoType}.jpg"
        val file = File(filesDir, fileName)

        try {
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal menyimpan gambar: ${e.message}", Toast.LENGTH_SHORT).show()
            return ""
        }
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

