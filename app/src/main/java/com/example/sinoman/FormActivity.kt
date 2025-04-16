package com.example.sinoman

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.text.InputFilter
import android.text.Spanned
import android.text.TextWatcher
import android.location.LocationManager
import android.provider.Settings
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

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
    private lateinit var badConditionRadioButton: Button
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

    // Assistance Type
    private lateinit var assistanceTypeRadioGroup: RadioGroup
    private lateinit var houseAssistanceRadioButton: RadioButton
    private lateinit var apartmentAssistanceRadioButton: RadioButton
    private lateinit var houseAssistanceQuestionsLayout: LinearLayout
    private lateinit var apartmentAssistanceQuestionsLayout: LinearLayout

    // House Assistance Questions
    private lateinit var houseQuestion1EditText: TextInputEditText
    private lateinit var houseQuestion1InputLayout: TextInputLayout
    private lateinit var houseQuestion2EditText: TextInputEditText
    private lateinit var houseQuestion2InputLayout: TextInputLayout
    private lateinit var houseQuestion3EditText: TextInputEditText
    private lateinit var houseQuestion3InputLayout: TextInputLayout

    // Apartment Assistance Questions
    private lateinit var apartmentQuestion1EditText: TextInputEditText
    private lateinit var apartmentQuestion1InputLayout: TextInputLayout
    private lateinit var apartmentQuestion2EditText: TextInputEditText
    private lateinit var apartmentQuestion2InputLayout: TextInputLayout
    private lateinit var apartmentQuestion3EditText: TextInputEditText
    private lateinit var apartmentQuestion3InputLayout: TextInputLayout

    // Location
    private lateinit var getCurrentLocationButton: Button
    private lateinit var selectLocationButton: Button
    private lateinit var locationPreviewContainer: View
    private lateinit var latitudeEditText: TextInputEditText
    private lateinit var longitudeEditText: TextInputEditText
    private lateinit var locationAddressEditText: TextInputEditText
    private lateinit var locationAddressInputLayout: TextInputLayout
    private lateinit var locationPreviewImage: ImageView
    private lateinit var locationPreviewPlaceholder: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionCode = 2000
    private val mapPickerRequestCode = 2001

    private var latitude = 0.0
    private var longitude = 0.0

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

    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    private val LOCATION_SETTINGS_REQUEST_CODE = 2002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        // Initialize views
        initializeViews()

        // Initialize map view
        initializeMapView(savedInstanceState)

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

        // Set up assistance type radio group
        setupAssistanceTypeRadioGroup()

        // Set up location buttons
        setupLocationButtons()

        // Set up save button
        saveButton.text = "Kirim"
        saveButton.setOnClickListener {
            if (validateForm()) {
                saveFormData()
                FormData.setPersonalDataCompleted(this, true)

                // Create a new registration based on the selected assistance type
                val registrationType = when (assistanceTypeRadioGroup.checkedRadioButtonId) {
                    R.id.houseAssistanceRadioButton -> RegistrationType.HOUSE_OWNER
                    R.id.apartmentAssistanceRadioButton -> RegistrationType.NON_HOUSE_OWNER
                    else -> RegistrationType.HOUSE_OWNER // Default fallback
                }

                // Create and save the registration
                val registration = RegistrationData(
                    type = registrationType,
                    status = RegistrationStatus.UNDER_REVIEW,
                    dateCreated = System.currentTimeMillis(),
                    dateUpdated = System.currentTimeMillis()
                )

                // Add specific data based on assistance type
                if (registrationType == RegistrationType.HOUSE_OWNER) {
                    registration.buildingSize = "N/A"
                    registration.landSize = houseQuestion1EditText.text.toString()
                    registration.certificateType = houseQuestion2EditText.text.toString()
                    registration.financingInterest = houseQuestion3EditText.text.toString()
                } else {
                    registration.houseInterest = "N/A"
                    registration.locationProvince = apartmentQuestion1EditText.text.toString()
                    registration.locationCity = apartmentQuestion2EditText.text.toString()
                    registration.locationDistrict = apartmentQuestion3EditText.text.toString()
                    registration.locationVillage = "N/A"
                }

                RegistrationData.addOrUpdateRegistration(this, registration)

                // Update shared preferences to indicate submission
                val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                prefs.edit().putBoolean("has_submitted_application", true).apply()

                // Update verification status to UNDER_REVIEW
                prefs.edit().putInt("verification_status", DashboardActivity.STATUS_UNDER_REVIEW).apply()

                // Navigate to FormPage2Activity
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

        // Assistance Type
        assistanceTypeRadioGroup = findViewById(R.id.assistanceTypeRadioGroup)
        houseAssistanceRadioButton = findViewById(R.id.houseAssistanceRadioButton)
        apartmentAssistanceRadioButton = findViewById(R.id.apartmentAssistanceRadioButton)
        houseAssistanceQuestionsLayout = findViewById(R.id.houseAssistanceQuestionsLayout)
        apartmentAssistanceQuestionsLayout = findViewById(R.id.apartmentAssistanceQuestionsLayout)

        // House Assistance Questions
        houseQuestion1EditText = findViewById(R.id.houseQuestion1EditText)
        houseQuestion1InputLayout = findViewById(R.id.houseQuestion1InputLayout)
        houseQuestion2EditText = findViewById(R.id.houseQuestion2EditText)
        houseQuestion2InputLayout = findViewById(R.id.houseQuestion2InputLayout)
        houseQuestion3EditText = findViewById(R.id.houseQuestion3EditText)
        houseQuestion3InputLayout = findViewById(R.id.houseQuestion3InputLayout)

        // Apartment Assistance Questions
        apartmentQuestion1EditText = findViewById(R.id.apartmentQuestion1EditText)
        apartmentQuestion1InputLayout = findViewById(R.id.apartmentQuestion1InputLayout)
        apartmentQuestion2EditText = findViewById(R.id.apartmentQuestion2EditText)
        apartmentQuestion2InputLayout = findViewById(R.id.apartmentQuestion2InputLayout)
        apartmentQuestion3EditText = findViewById(R.id.apartmentQuestion3EditText)
        apartmentQuestion3InputLayout = findViewById(R.id.apartmentQuestion3InputLayout)

        // Location-related views
        getCurrentLocationButton = findViewById(R.id.getCurrentLocationButton)
        selectLocationButton = findViewById(R.id.selectLocationButton)
        locationPreviewContainer = findViewById(R.id.locationPreviewContainer)
        latitudeEditText = findViewById(R.id.latitudeEditText)
        longitudeEditText = findViewById(R.id.longitudeEditText)
        locationAddressEditText = findViewById(R.id.locationAddressEditText)
        locationAddressInputLayout = findViewById(R.id.locationAddressInputLayout)
        locationPreviewImage = findViewById(R.id.locationPreviewImage)
        locationPreviewPlaceholder = findViewById(R.id.locationPreviewPlaceholder)

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Agreement
        agreementCheckBox = findViewById(R.id.agreementCheckBox)
        signatureEditText = findViewById(R.id.signatureEditText)
        signatureInputLayout = findViewById(R.id.signatureInputLayout)
        registrationDateEditText = findViewById(R.id.registrationDateEditText)
        registrationDateInputLayout = findViewById(R.id.registrationDateInputLayout)

        saveButton = findViewById(R.id.saveButton)

        // Set input filters to limit family members and child ages to 2 digits
        val twoDigitFilter = InputFilter.LengthFilter(2)
        val numberFilter = object : InputFilter {
            override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
                for (i in start until end) {
                    if (!Character.isDigit(source[i])) {
                        return ""
                    }
                }
                return null
            }
        }

        // Apply filters to family members field
        familyMembersEditText.filters = arrayOf(twoDigitFilter, numberFilter)
        familyMembersEditText.inputType = android.text.InputType.TYPE_CLASS_NUMBER

        // Apply filters to child age fields
        child1AgeEditText.filters = arrayOf(twoDigitFilter, numberFilter)
        child1AgeEditText.inputType = android.text.InputType.TYPE_CLASS_NUMBER

        child2AgeEditText.filters = arrayOf(twoDigitFilter, numberFilter)
        child2AgeEditText.inputType = android.text.InputType.TYPE_CLASS_NUMBER

        child3AgeEditText.filters = arrayOf(twoDigitFilter, numberFilter)
        child3AgeEditText.inputType = android.text.InputType.TYPE_CLASS_NUMBER
    }

    private fun initializeMapView(savedInstanceState: Bundle?) {
        // Initialize MapView
        mapView = findViewById(R.id.mapView)

        // Create Bundle for MapView if needed
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        // Initialize and setup MapView
        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(map: GoogleMap) {
                googleMap = map
                googleMap?.uiSettings?.isZoomControlsEnabled = true
                googleMap?.uiSettings?.isCompassEnabled = true

                // If we already have coordinates, show them on the map
                if (latitude != 0.0 && longitude != 0.0) {
                    showLocationOnMap(latitude, longitude)
                }
            }
        })
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showLocationServicesDialog() {
        AlertDialog.Builder(this)
            .setTitle("Lokasi Tidak Aktif")
            .setMessage("Layanan lokasi dinonaktifkan. Silakan aktifkan untuk menggunakan fitur ini.")
            .setPositiveButton("Pengaturan") { _, _ ->
                // Open location settings
                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                    LOCATION_SETTINGS_REQUEST_CODE
                )
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(
                    this,
                    "Fitur ini memerlukan akses lokasi",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .setCancelable(false)
            .show()
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

    private fun setupAssistanceTypeRadioGroup() {
        assistanceTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.houseAssistanceRadioButton -> {
                    houseAssistanceQuestionsLayout.visibility = View.VISIBLE
                    apartmentAssistanceQuestionsLayout.visibility = View.GONE

                    // Save assistance type to shared preferences
                    val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    prefs.edit().putInt("assistance_type", DashboardActivity.ASSISTANCE_WITH_HOME).apply()
                }
                R.id.apartmentAssistanceRadioButton -> {
                    houseAssistanceQuestionsLayout.visibility = View.GONE
                    apartmentAssistanceQuestionsLayout.visibility = View.VISIBLE

                    // Save assistance type to shared preferences
                    val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    prefs.edit().putInt("assistance_type", DashboardActivity.ASSISTANCE_WITHOUT_HOME).apply()
                }
                else -> {
                    houseAssistanceQuestionsLayout.visibility = View.GONE
                    apartmentAssistanceQuestionsLayout.visibility = View.GONE

                    // Reset assistance type in shared preferences
                    val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                    prefs.edit().putInt("assistance_type", DashboardActivity.ASSISTANCE_NONE).apply()
                }
            }
            formChanged = true
        }
    }

    private fun setupLocationButtons() {
        getCurrentLocationButton.setOnClickListener {
            requestLocationPermissions()
        }

        selectLocationButton.setOnClickListener {
            val intent = Intent(this, LocationPickerActivity::class.java)
            // If we already have coordinates, pass them to the picker
            if (latitude != 0.0 && longitude != 0.0) {
                intent.putExtra(LocationPickerActivity.EXTRA_LATITUDE, latitude)
                intent.putExtra(LocationPickerActivity.EXTRA_LONGITUDE, longitude)
            }
            startActivityForResult(intent, mapPickerRequestCode)
        }
    }

    private fun requestLocationPermissions() {
        // First check if location services are enabled
        if (!isLocationEnabled()) {
            showLocationServicesDialog()
            return
        }

        // Then check for permissions
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                locationPermissionCode
            )
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        try {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Mendapatkan lokasi...", Toast.LENGTH_SHORT).show()

                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            // Got the location
                            updateLocationUi(
                                location.latitude,
                                location.longitude
                            )

                            // Get address from location
                            getAddressFromLocation(location)
                        } else {
                            Toast.makeText(
                                this,
                                "Lokasi tidak tersedia. Silakan coba lagi nanti atau gunakan 'Pilih di Peta'.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Gagal mendapatkan lokasi: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        } catch (e: SecurityException) {
            Toast.makeText(
                this,
                "Error izin lokasi: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getAddressFromLocation(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())

        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val addressText = buildAddressString(address)
                locationAddressEditText.setText(addressText)
            } else {
                locationAddressEditText.setText("Alamat tidak ditemukan")
            }
        } catch (e: IOException) {
            locationAddressEditText.setText("Error: ${e.message}")
        }
    }

    private fun buildAddressString(address: Address): String {
        val sb = StringBuilder()

        for (i in 0..address.maxAddressLineIndex) {
            sb.append(address.getAddressLine(i))
            if (i < address.maxAddressLineIndex) {
                sb.append(", ")
            }
        }

        return sb.toString()
    }

    private fun bitmapDescriptorFromVector(vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(this, vectorResId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun showLocationOnMap(lat: Double, lng: Double) {
        googleMap?.let { map ->
            val location = LatLng(lat, lng)
            map.clear()

            // Add marker at the location
            map.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Lokasi Terpilih")
                    .icon(bitmapDescriptorFromVector(R.drawable.ic_map_marker))
            )

            // Move camera to the location with zoom
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

            // Show the map container
            locationPreviewContainer.visibility = View.VISIBLE
            locationPreviewPlaceholder.visibility = View.GONE
            mapView.visibility = View.VISIBLE
        }
    }

    private fun updateLocationUi(lat: Double, lng: Double) {
        // Update class variables
        latitude = lat
        longitude = lng

        // Update UI
        latitudeEditText.setText(String.format(Locale.US, "%.6f", lat))
        longitudeEditText.setText(String.format(Locale.US, "%.6f", lng))

        // Show the location preview container
        locationPreviewContainer.visibility = View.VISIBLE

        // Show location on map
        showLocationOnMap(lat, lng)

        // Mark that form has changed
        formChanged = true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            locationPermissionCode -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                } else {
                    // Show dialog explaining why location is needed and guide to settings
                    AlertDialog.Builder(this)
                        .setTitle("Izin Lokasi Diperlukan")
                        .setMessage("Aplikasi memerlukan izin lokasi untuk menampilkan lokasi Anda di peta. Silakan aktifkan di pengaturan aplikasi.")
                        .setPositiveButton("Pengaturan") { _, _ ->
                            // Open app settings
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        }
                        .setNegativeButton("Batal") { dialog, _ ->
                            dialog.dismiss()
                            Toast.makeText(
                                this,
                                "Fitur ini memerlukan akses lokasi",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .setCancelable(false)
                        .show()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView.onSaveInstanceState(mapViewBundle)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == mapPickerRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            // Get the selected location from the location picker
            val selectedLatitude = data.getDoubleExtra(LocationPickerActivity.EXTRA_LATITUDE, 0.0)
            val selectedLongitude = data.getDoubleExtra(LocationPickerActivity.EXTRA_LONGITUDE, 0.0)
            val selectedAddress = data.getStringExtra(LocationPickerActivity.EXTRA_ADDRESS) ?: ""

            // Update UI with the selected location
            updateLocationUi(selectedLatitude, selectedLongitude)
            locationAddressEditText.setText(selectedAddress)
        }
        else if (requestCode == LOCATION_SETTINGS_REQUEST_CODE) {
            // Check if location is now enabled after returning from settings
            if (isLocationEnabled()) {
                requestLocationPermissions() // This will now proceed to check permissions
            } else {
                Toast.makeText(
                    this,
                    "Lokasi masih tidak aktif. Beberapa fitur mungkin tidak berfungsi.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun populateFormWithData() {
        // Personal Data
        nameEditText.setText(formData.name)
        nikEditText.setText(formData.nik)
        birthplaceEditText.setText(formData.hometownAddress)
        addressEditText.setText(formData.address)
        phoneEditText.setText(formData.phone)
        emailEditText.setText("")

        // Set gender
        when (formData.gender) {
            "Laki-laki" -> maleRadioButton.isChecked = true
            "Perempuan" -> femaleRadioButton.isChecked = true
        }

        // Family Data
        when (formData.maritalStatus) {
            "Belum Menikah" -> singleRadioButton.isChecked = true
            "Menikah" -> marriedRadioButton.isChecked = true
            "Duda/Janda" -> widowedRadioButton.isChecked = true
        }

        familyMembersEditText.setText(formData.numberOfChildren)

        // Economic Data
        occupationEditText.setText(formData.occupation)
        companyEditText.setText("")
        incomeEditText.setText(formData.monthlyIncome)

        // Housing Data
        currentAddressEditText.setText(formData.currentAddress)

        // Set registration date
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        registrationDateEditText.setText(dateFormat.format(Date()))

        // Check if document paths are already set
        if (formData.nikPhotoPath.isNotBlank()) {
            ktpDocPath = formData.nikPhotoPath
            ktpCheckBox.isEnabled = true
            ktpCheckBox.isChecked = true
        }

        if (formData.familyCardPhotoPath.isNotBlank()) {
            kkDocPath = formData.familyCardPhotoPath
            kkCheckBox.isEnabled = true
            kkCheckBox.isChecked = true
        }

        // Check for assistance type in shared preferences
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val assistanceType = prefs.getInt("assistance_type", DashboardActivity.ASSISTANCE_NONE)

        when (assistanceType) {
            DashboardActivity.ASSISTANCE_WITH_HOME -> {
                houseAssistanceRadioButton.isChecked = true
                houseAssistanceQuestionsLayout.visibility = View.VISIBLE
            }
            DashboardActivity.ASSISTANCE_WITHOUT_HOME -> {
                apartmentAssistanceRadioButton.isChecked = true
                apartmentAssistanceQuestionsLayout.visibility = View.VISIBLE
            }
        }

        // Set location data if available
        if (formData.latitude != 0.0 && formData.longitude != 0.0) {
            latitude = formData.latitude
            longitude = formData.longitude
            latitudeEditText.setText(String.format(Locale.US, "%.6f", formData.latitude))
            longitudeEditText.setText(String.format(Locale.US, "%.6f", formData.longitude))
            locationAddressEditText.setText(formData.locationAddress)
            locationPreviewContainer.visibility = View.VISIBLE
            locationPreviewPlaceholder.visibility = View.VISIBLE
            locationPreviewPlaceholder.text = "Lokasi: ${formData.latitude}, ${formData.longitude}"
        }
    }

    private fun setupFormChangeListeners() {
        // Set up text field focus change listeners
        val textInputLayouts = listOf(
            nameInputLayout, nikInputLayout, birthplaceInputLayout, addressInputLayout,
            phoneInputLayout, emailInputLayout, familyMembersInputLayout, spouseNameInputLayout,
            occupationInputLayout, companyInputLayout, incomeInputLayout, socialAidDetailInputLayout,
            currentAddressInputLayout, reasonInputLayout, signatureInputLayout,
            houseQuestion1InputLayout, houseQuestion2InputLayout, houseQuestion3InputLayout,
            apartmentQuestion1InputLayout, apartmentQuestion2InputLayout, apartmentQuestion3InputLayout
        )

        val textInputEditTexts = listOf(
            nameEditText, nikEditText, birthplaceEditText, addressEditText,
            phoneEditText, emailEditText, familyMembersEditText, spouseNameEditText,
            occupationEditText, companyEditText, incomeEditText, socialAidDetailEditText,
            currentAddressEditText, reasonEditText, signatureEditText,
            houseQuestion1EditText, houseQuestion2EditText, houseQuestion3EditText,
            apartmentQuestion1EditText, apartmentQuestion2EditText, apartmentQuestion3EditText
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
            housingStatusRadioGroup, housingConditionRadioGroup, assistanceTypeRadioGroup
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

        // Add text changed listener for family members field
        familyMembersEditText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: android.text.Editable?) {
                if (!s.isNullOrEmpty() && s.toString().toIntOrNull() != null) {
                    val value = s.toString().toInt()
                    if (value > 99) {
                        familyMembersEditText.setText("99")
                        familyMembersEditText.setSelection(2)
                    }
                }
                formChanged = true
            }
        })

        // Add text changed listeners for child age fields
        val childAgeFields = listOf(child1AgeEditText, child2AgeEditText, child3AgeEditText)

        for (ageField in childAgeFields) {
            ageField.addTextChangedListener(object : android.text.TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: android.text.Editable?) {
                    if (!s.isNullOrEmpty() && s.toString().toIntOrNull() != null) {
                        val value = s.toString().toInt()
                        if (value > 99) {
                            ageField.setText("99")
                            ageField.setSelection(2)
                        }
                    }
                    formChanged = true
                }
            })
        }
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

        // Validate family members count is within reasonable range (1-99)
        val familyMembersText = familyMembersEditText.text.toString()
        if (familyMembersText.isNotBlank()) {
            val familyMembersCount = familyMembersText.toInt()
            if (familyMembersCount < 1) {
                familyMembersInputLayout.error = "Jumlah anggota keluarga minimal 1"
                isValid = false
            } else {
                familyMembersInputLayout.error = null
            }
        }

        // Validate child ages are within reasonable range (0-99)
        val childAgeFields = listOf(
            Pair(child1AgeEditText, "Anak 1"),
            Pair(child2AgeEditText, "Anak 2"),
            Pair(child3AgeEditText, "Anak 3")
        )

        for ((ageField, childLabel) in childAgeFields) {
            val ageText = ageField.text.toString()
            if (ageText.isNotBlank()) {
                val age = ageText.toInt()
                if (age < 0 || age > 99) {
                    Toast.makeText(this, "$childLabel: Umur harus antara 0-99 tahun", Toast.LENGTH_SHORT).show()
                    isValid = false
                }
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

        // Validate assistance type selection
        if (assistanceTypeRadioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Pilih jenis bantuan yang diajukan", Toast.LENGTH_SHORT).show()
            isValid = false
        } else {
            // Validate assistance type specific questions
            when (assistanceTypeRadioGroup.checkedRadioButtonId) {
                R.id.houseAssistanceRadioButton -> {
                    val houseQuestionFields = listOf(
                        Pair(houseQuestion1InputLayout, houseQuestion1EditText.text.toString()),
                        Pair(houseQuestion2InputLayout, houseQuestion2EditText.text.toString()),
                        Pair(houseQuestion3InputLayout, houseQuestion3EditText.text.toString())
                    )

                    for ((layout, value) in houseQuestionFields) {
                        if (!validateField(layout, value)) {
                            isValid = false
                        }
                    }

                    // Add location validation
                    if (latitude == 0.0 || longitude == 0.0) {
                        Toast.makeText(this, "Silakan pilih lokasi tanah/bangunan", Toast.LENGTH_SHORT).show()
                        isValid = false
                    }

                    if (locationAddressEditText.text.toString().isBlank()) {
                        locationAddressInputLayout.error = "Alamat lokasi harus diisi"
                        isValid = false
                    } else {
                        locationAddressInputLayout.error = null
                    }
                }

                R.id.apartmentAssistanceRadioButton -> {
                    val apartmentQuestionFields = listOf(
                        Pair(apartmentQuestion1InputLayout, apartmentQuestion1EditText.text.toString()),
                        Pair(apartmentQuestion2InputLayout, apartmentQuestion2EditText.text.toString()),
                        Pair(apartmentQuestion3InputLayout, apartmentQuestion3EditText.text.toString())
                    )

                    for ((layout, value) in apartmentQuestionFields) {
                        if (!validateField(layout, value)) {
                            isValid = false
                        }
                    }
                }
            }
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
        // Save form data to FormData object
        formData.name = nameEditText.text.toString()
        formData.nik = nikEditText.text.toString()
        formData.nikPhotoPath = ktpDocPath
        formData.familyCardPhotoPath = kkDocPath
        formData.address = addressEditText.text.toString()
        formData.currentAddress = currentAddressEditText.text.toString()
        formData.hometownAddress = birthplaceEditText.text.toString()

        // Save gender
        formData.gender = when (genderRadioGroup.checkedRadioButtonId) {
            R.id.maleRadioButton -> "Laki-laki"
            R.id.femaleRadioButton -> "Perempuan"
            else -> ""
        }

        // Save marital status
        formData.maritalStatus = when (maritalStatusRadioGroup.checkedRadioButtonId) {
            R.id.singleRadioButton -> "Belum Menikah"
            R.id.marriedRadioButton -> "Menikah"
            R.id.widowedRadioButton -> "Duda/Janda"
            else -> ""
        }

        formData.numberOfChildren = familyMembersEditText.text.toString()
        formData.occupation = occupationEditText.text.toString()
        formData.monthlyIncome = incomeEditText.text.toString()
        formData.phone = phoneEditText.text.toString()

        // Save location data
        formData.latitude = latitude
        formData.longitude = longitude
        formData.locationAddress = locationAddressEditText.text.toString()

        // Save assistance type specific data
        when (assistanceTypeRadioGroup.checkedRadioButtonId) {
            R.id.houseAssistanceRadioButton -> {
                formData.landSize = houseQuestion1EditText.text.toString()
                formData.certificateType = houseQuestion2EditText.text.toString()
                formData.financingInterest = houseQuestion3EditText.text.toString()
            }
            R.id.apartmentAssistanceRadioButton -> {
                formData.locationProvince = apartmentQuestion1EditText.text.toString()
                formData.locationCity = apartmentQuestion2EditText.text.toString()
                formData.locationDistrict = apartmentQuestion3EditText.text.toString()
            }
        }

        // Save form data to storage
        FormData.save(this, formData)

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

    // Add a helper method to validate numeric input with max value
    // Add this method to the FormActivity class
    private fun validateNumericField(inputLayout: TextInputLayout, value: String, fieldName: String, maxValue: Int): Boolean {
        if (value.isBlank()) {
            inputLayout.error = "Field ini harus diisi"
            return false
        }

        try {
            val numValue = value.toInt()
            if (numValue < 0 || numValue > maxValue) {
                inputLayout.error = "$fieldName harus antara 0-$maxValue"
                return false
            }
        } catch (e: NumberFormatException) {
            inputLayout.error = "$fieldName harus berupa angka"
            return false
        }

        inputLayout.error = null
        return true
    }
}
