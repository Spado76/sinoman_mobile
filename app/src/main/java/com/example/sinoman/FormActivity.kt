package com.example.sinoman

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FormActivity : AppCompatActivity() {

    // Personal Data
    private lateinit var nameEditText: TextInputEditText
    private lateinit var nameInputLayout: TextInputLayout
    private lateinit var nikEditText: TextInputEditText
    private lateinit var nikInputLayout: TextInputLayout
    private lateinit var birthplaceEditText: TextInputEditText
    private lateinit var birthplaceInputLayout: TextInputLayout
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton
    private lateinit var addressEditText: TextInputEditText
    private lateinit var addressInputLayout: TextInputLayout
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var phoneInputLayout: TextInputLayout
    private lateinit var emailEditText: TextInputEditText
    private lateinit var emailInputLayout: TextInputLayout

    // Family Data
    private lateinit var maritalStatusRadioGroup: RadioGroup
    private lateinit var singleRadioButton: RadioButton
    private lateinit var marriedRadioButton: RadioButton
    private lateinit var widowedRadioButton: RadioButton
    private lateinit var familyMembersEditText: TextInputEditText
    private lateinit var familyMembersInputLayout: TextInputLayout
    private lateinit var spouseNameEditText: TextInputEditText
    private lateinit var spouseNameInputLayout: TextInputLayout
    private lateinit var child1NameEditText: TextInputEditText
    private lateinit var child1AgeEditText: TextInputEditText
    private lateinit var child2NameEditText: TextInputEditText
    private lateinit var child2AgeEditText: TextInputEditText
    private lateinit var child3NameEditText: TextInputEditText
    private lateinit var child3AgeEditText: TextInputEditText

    // Economic Data
    private lateinit var occupationEditText: TextInputEditText
    private lateinit var occupationInputLayout: TextInputLayout
    private lateinit var companyEditText: TextInputEditText
    private lateinit var companyInputLayout: TextInputLayout
    private lateinit var incomeEditText: TextInputEditText
    private lateinit var incomeInputLayout: TextInputLayout
    private lateinit var socialAidRadioGroup: RadioGroup
    private lateinit var socialAidYesRadioButton: RadioButton
    private lateinit var socialAidNoRadioButton: RadioButton
    private lateinit var socialAidDetailEditText: TextInputEditText
    private lateinit var socialAidDetailInputLayout: TextInputLayout

    // Housing Data
    private lateinit var housingStatusRadioGroup: RadioGroup
    private lateinit var ownedRadioButton: RadioButton
    private lateinit var rentedRadioButton: RadioButton
    private lateinit var familyOwnedRadioButton: RadioButton
    private lateinit var currentAddressEditText: TextInputEditText
    private lateinit var currentAddressInputLayout: TextInputLayout
    private lateinit var housingConditionRadioGroup: RadioGroup
    private lateinit var goodConditionRadioButton: RadioButton
    private lateinit var badConditionRadioButton: RadioButton
    private lateinit var reasonEditText: TextInputEditText
    private lateinit var reasonInputLayout: TextInputLayout

    // Documents
    private lateinit var ktpCheckBox: CheckBox
    private lateinit var ktpUploadButton: Button
    private lateinit var kkCheckBox: CheckBox
    private lateinit var kkUploadButton: Button
    private lateinit var salarySlipCheckBox: CheckBox
    private lateinit var salarySlipUploadButton: Button
    private lateinit var poorLetterCheckBox: CheckBox
    private lateinit var poorLetterUploadButton: Button
    private lateinit var otherDocsCheckBox: CheckBox
    private lateinit var otherDocsUploadButton: Button

    // Agreement
    private lateinit var agreementCheckBox: CheckBox
    private lateinit var signatureEditText: TextInputEditText
    private lateinit var signatureInputLayout: TextInputLayout
    private lateinit var registrationDateEditText: TextInputEditText
    private lateinit var registrationDateInputLayout: TextInputLayout

    private lateinit var saveButton: Button

    private var formData = FormData()
    private var formChanged = false
    private var currentDocType = ""

    // Document upload paths
    private var ktpDocPath = ""
    private var kkDocPath = ""
    private var salarySlipDocPath = ""
    private var poorLetterDocPath = ""
    private var otherDocsPath = ""

    // Document picker launcher
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val docPath = saveDocumentToInternalStorage(uri)
            when (currentDocType) {
                "ktp" -> {
                    ktpDocPath = docPath
                    ktpCheckBox.isChecked = true
                }
                "kk" -> {
                    kkDocPath = docPath
                    kkCheckBox.isChecked = true
                }
                "salary_slip" -> {
                    salarySlipDocPath = docPath
                    salarySlipCheckBox.isChecked = true
                }
                "poor_letter" -> {
                    poorLetterDocPath = docPath
                    poorLetterCheckBox.isChecked = true
                }
                "other_docs" -> {
                    otherDocsPath = docPath
                    otherDocsCheckBox.isChecked = true
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

        // Load saved form data
        formData = FormData.load(this)
        populateFormWithData()

        // Set up form change listeners
        setupFormChangeListeners()

        // Set up document upload buttons
        setupDocumentButtons()

        // Set up date picker for registration date
        setupDatePicker()

        // Set up save button
        saveButton.setOnClickListener {
            if (validateForm()) {
                saveFormData()
                FormData.setPersonalDataCompleted(this, true)
                val intent = Intent(this, FormPage2Activity::class.java)
                startActivity(intent)
                finish() // Close this activity so user can't go back with back button
            }
        }
    }

    private fun initializeViews() {
        // Personal Data
        nameEditText = findViewById(R.id.nameEditText)
        nameInputLayout = findViewById(R.id.nameInputLayout)
        nikEditText = findViewById(R.id.nikEditText)
        nikInputLayout = findViewById(R.id.nikInputLayout)
        birthplaceEditText = findViewById(R.id.birthplaceEditText)
        birthplaceInputLayout = findViewById(R.id.birthplaceInputLayout)
        genderRadioGroup = findViewById(R.id.genderRadioGroup)
        maleRadioButton = findViewById(R.id.maleRadioButton)
        femaleRadioButton = findViewById(R.id.femaleRadioButton)
        addressEditText = findViewById(R.id.addressEditText)
        addressInputLayout = findViewById(R.id.addressInputLayout)
        phoneEditText = findViewById(R.id.phoneEditText)
        phoneInputLayout = findViewById(R.id.phoneInputLayout)
        emailEditText = findViewById(R.id.emailEditText)
        emailInputLayout = findViewById(R.id.emailInputLayout)

        // Family Data
        maritalStatusRadioGroup = findViewById(R.id.maritalStatusRadioGroup)
        singleRadioButton = findViewById(R.id.singleRadioButton)
        marriedRadioButton = findViewById(R.id.marriedRadioButton)
        widowedRadioButton = findViewById(R.id.widowedRadioButton)
        familyMembersEditText = findViewById(R.id.familyMembersEditText)
        familyMembersInputLayout = findViewById(R.id.familyMembersInputLayout)
        spouseNameEditText = findViewById(R.id.spouseNameEditText)
        spouseNameInputLayout = findViewById(R.id.spouseNameInputLayout)
        child1NameEditText = findViewById(R.id.child1NameEditText)
        child1AgeEditText = findViewById(R.id.child1AgeEditText)
        child2NameEditText = findViewById(R.id.child2NameEditText)
        child2AgeEditText = findViewById(R.id.child2AgeEditText)
        child3NameEditText = findViewById(R.id.child3NameEditText)
        child3AgeEditText = findViewById(R.id.child3AgeEditText)

        // Economic Data
        occupationEditText = findViewById(R.id.occupationEditText)
        occupationInputLayout = findViewById(R.id.occupationInputLayout)
        companyEditText = findViewById(R.id.companyEditText)
        companyInputLayout = findViewById(R.id.companyInputLayout)
        incomeEditText = findViewById(R.id.incomeEditText)
        incomeInputLayout = findViewById(R.id.incomeInputLayout)
        socialAidRadioGroup = findViewById(R.id.socialAidRadioGroup)
        socialAidYesRadioButton = findViewById(R.id.socialAidYesRadioButton)
        socialAidNoRadioButton = findViewById(R.id.socialAidNoRadioButton)
        socialAidDetailEditText = findViewById(R.id.socialAidDetailEditText)
        socialAidDetailInputLayout = findViewById(R.id.socialAidDetailInputLayout)

        // Housing Data
        housingStatusRadioGroup = findViewById(R.id.housingStatusRadioGroup)
        ownedRadioButton = findViewById(R.id.ownedRadioButton)
        rentedRadioButton = findViewById(R.id.rentedRadioButton)
        familyOwnedRadioButton = findViewById(R.id.familyOwnedRadioButton)
        currentAddressEditText = findViewById(R.id.currentAddressEditText)
        currentAddressInputLayout = findViewById(R.id.currentAddressInputLayout)
        housingConditionRadioGroup = findViewById(R.id.housingConditionRadioGroup)
        goodConditionRadioButton = findViewById(R.id.goodConditionRadioButton)
        badConditionRadioButton = findViewById(R.id.badConditionRadioButton)
        reasonEditText = findViewById(R.id.reasonEditText)
        reasonInputLayout = findViewById(R.id.reasonInputLayout)

        // Documents
        ktpCheckBox = findViewById(R.id.ktpCheckBox)
        ktpUploadButton = findViewById(R.id.ktpUploadButton)
        kkCheckBox = findViewById(R.id.kkCheckBox)
        kkUploadButton = findViewById(R.id.kkUploadButton)
        salarySlipCheckBox = findViewById(R.id.salarySlipCheckBox)
        salarySlipUploadButton = findViewById(R.id.salarySlipUploadButton)
        poorLetterCheckBox = findViewById(R.id.poorLetterCheckBox)
        poorLetterUploadButton = findViewById(R.id.poorLetterUploadButton)
        otherDocsCheckBox = findViewById(R.id.otherDocsCheckBox)
        otherDocsUploadButton = findViewById(R.id.otherDocsUploadButton)

        // Agreement
        agreementCheckBox = findViewById(R.id.agreementCheckBox)
        signatureEditText = findViewById(R.id.signatureEditText)
        signatureInputLayout = findViewById(R.id.signatureInputLayout)
        registrationDateEditText = findViewById(R.id.registrationDateEditText)
        registrationDateInputLayout = findViewById(R.id.registrationDateInputLayout)

        saveButton = findViewById(R.id.saveButton)
    }

    private fun setupDocumentButtons() {
        ktpUploadButton.setOnClickListener {
            currentDocType = "ktp"
            getContent.launch("*/*")
        }

        kkUploadButton.setOnClickListener {
            currentDocType = "kk"
            getContent.launch("*/*")
        }

        salarySlipUploadButton.setOnClickListener {
            currentDocType = "salary_slip"
            getContent.launch("*/*")
        }

        poorLetterUploadButton.setOnClickListener {
            currentDocType = "poor_letter"
            getContent.launch("*/*")
        }

        otherDocsUploadButton.setOnClickListener {
            currentDocType = "other_docs"
            getContent.launch("*/*")
        }
    }

    private fun setupDatePicker() {
        // Set current date as default
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        registrationDateEditText.setText(dateFormat.format(calendar.time))

        // Make the field non-editable
        registrationDateEditText.isEnabled = false
        registrationDateInputLayout.isEnabled = false
    }

    private fun populateFormWithData() {
        // This is a placeholder - you would need to adapt this to your actual FormData structure
        // For now, we'll just set the current date for the registration date
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        registrationDateEditText.setText(dateFormat.format(Date()))
    }

    private fun setupFormChangeListeners() {
        // Set up text field focus change listeners
        val textInputLayouts = listOf(
            nameInputLayout, nikInputLayout, birthplaceInputLayout, addressInputLayout,
            phoneInputLayout, emailInputLayout, familyMembersInputLayout, spouseNameInputLayout,
            occupationInputLayout, companyInputLayout, incomeInputLayout, socialAidDetailInputLayout,
            currentAddressInputLayout, reasonInputLayout, signatureInputLayout
        )

        val textInputEditTexts = listOf(
            nameEditText, nikEditText, birthplaceEditText, addressEditText,
            phoneEditText, emailEditText, familyMembersEditText, spouseNameEditText,
            occupationEditText, companyEditText, incomeEditText, socialAidDetailEditText,
            currentAddressEditText, reasonEditText, signatureEditText
        )

        for (i in textInputLayouts.indices) {
            textInputEditTexts[i].setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    validateField(textInputLayouts[i], textInputEditTexts[i].text.toString())
                    formChanged = true
                }
            }
        }

        // Set up radio group change listeners
        val radioGroups = listOf(
            genderRadioGroup, maritalStatusRadioGroup, socialAidRadioGroup,
            housingStatusRadioGroup, housingConditionRadioGroup
        )

        for (radioGroup in radioGroups) {
            radioGroup.setOnCheckedChangeListener { _, _ ->
                formChanged = true
            }
        }

        // Set up checkbox change listeners
        val checkBoxes = listOf(
            agreementCheckBox
        )

        for (checkBox in checkBoxes) {
            checkBox.setOnCheckedChangeListener { _, _ ->
                formChanged = true
            }
        }

        // Show/hide social aid detail field based on radio selection
        socialAidRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            socialAidDetailInputLayout.visibility = if (checkedId == R.id.socialAidYesRadioButton) {
                View.VISIBLE
            } else {
                View.GONE
            }
            formChanged = true
        }

        // Handle conditional requirement for spouse name based on marital status
        maritalStatusRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.marriedRadioButton) {
                // If "Menikah" is selected, make spouse name required
                spouseNameInputLayout.hint = "Nama Pasangan (Wajib diisi)"
            } else {
                // Otherwise, make it optional
                spouseNameInputLayout.hint = "Nama Pasangan (Jika Ada)"
            }
            formChanged = true
        }

        // Initialize social aid detail visibility
        socialAidDetailInputLayout.visibility = View.GONE
    }

    private fun validateField(inputLayout: TextInputLayout, value: String): Boolean {
        return if (value.isBlank()) {
            inputLayout.error = "Field ini harus diisi"
            false
        } else {
            inputLayout.error = null
            true
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        // Validate required text fields
        val requiredFields = listOf(
            Pair(nameInputLayout, nameEditText.text.toString()),
            Pair(nikInputLayout, nikEditText.text.toString()),
            Pair(birthplaceInputLayout, birthplaceEditText.text.toString()),
            Pair(addressInputLayout, addressEditText.text.toString()),
            Pair(phoneInputLayout, phoneEditText.text.toString()),
            Pair(emailInputLayout, emailEditText.text.toString()),
            Pair(familyMembersInputLayout, familyMembersEditText.text.toString()),
            Pair(occupationInputLayout, occupationEditText.text.toString()),
            Pair(companyInputLayout, companyEditText.text.toString()),
            Pair(incomeInputLayout, incomeEditText.text.toString()),
            Pair(currentAddressInputLayout, currentAddressEditText.text.toString()),
            Pair(reasonInputLayout, reasonEditText.text.toString()),
            Pair(signatureInputLayout, signatureEditText.text.toString())
        )

        for ((layout, value) in requiredFields) {
            if (!validateField(layout, value)) {
                isValid = false
            }
        }

        // Validate radio selections
        if (genderRadioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Pilih jenis kelamin", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (maritalStatusRadioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Pilih status perkawinan", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (socialAidRadioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Pilih status bantuan sosial", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (housingStatusRadioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Pilih status tempat tinggal", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (housingConditionRadioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Pilih kondisi tempat tinggal", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        // Validate social aid detail if "Yes" is selected
        if (socialAidYesRadioButton.isChecked && socialAidDetailEditText.text.toString().isBlank()) {
            socialAidDetailInputLayout.error = "Harap sebutkan bantuan sosial yang diterima"
            isValid = false
        }

        // Validate spouse name if "Menikah" is selected
        if (marriedRadioButton.isChecked && spouseNameEditText.text.toString().isBlank()) {
            spouseNameInputLayout.error = "Nama pasangan harus diisi jika status menikah"
            isValid = false
        } else {
            spouseNameInputLayout.error = null
        }

        // Validate that name matches signature
        if (nameEditText.text.toString() != signatureEditText.text.toString()) {
            signatureInputLayout.error = "Tanda tangan harus sama dengan nama lengkap"
            isValid = false
        } else {
            signatureInputLayout.error = null
        }

        // Validate document uploads
        if (!ktpCheckBox.isChecked) {
            Toast.makeText(this, "Harap unggah fotokopi KTP", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (!kkCheckBox.isChecked) {
            Toast.makeText(this, "Harap unggah fotokopi Kartu Keluarga", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (!salarySlipCheckBox.isChecked) {
            Toast.makeText(this, "Harap unggah slip gaji", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        // Validate agreement checkbox
        if (!agreementCheckBox.isChecked) {
            Toast.makeText(this, "Anda harus menyetujui pernyataan", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun saveFormData() {
        // This is a placeholder - you would need to adapt this to your actual FormData structure
        Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
        formChanged = false
    }

    private fun saveDocumentToInternalStorage(uri: Uri): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "DOC_${timeStamp}_${currentDocType}.pdf"
        val file = File(filesDir, fileName)

        try {
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            // Enable and check the corresponding checkbox
            when (currentDocType) {
                "ktp" -> {
                    ktpCheckBox.isEnabled = true
                    ktpCheckBox.isChecked = true
                }
                "kk" -> {
                    kkCheckBox.isEnabled = true
                    kkCheckBox.isChecked = true
                }
                "salary_slip" -> {
                    salarySlipCheckBox.isEnabled = true
                    salarySlipCheckBox.isChecked = true
                }
                "poor_letter" -> {
                    poorLetterCheckBox.isEnabled = true
                    poorLetterCheckBox.isChecked = true
                }
                "other_docs" -> {
                    otherDocsCheckBox.isEnabled = true
                    otherDocsCheckBox.isChecked = true
                }
            }

            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal menyimpan dokumen: ${e.message}", Toast.LENGTH_SHORT).show()
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
