package com.example.sinoman

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.Locale

class LocationPickerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var progressBar: ProgressBar
    private lateinit var addressTextView: TextView
    private lateinit var confirmButton: Button
    private var selectedLatLng: LatLng? = null
    private var selectedAddress: String = ""

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1234
        const val EXTRA_LATITUDE = "extra_latitude"
        const val EXTRA_LONGITUDE = "extra_longitude"
        const val EXTRA_ADDRESS = "extra_address"
        const val EXTRA_LOCATION_DATA = "extra_location_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_picker)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Pilih Lokasi"

        progressBar = findViewById(R.id.progressBar)
        addressTextView = findViewById(R.id.addressTextView)
        confirmButton = findViewById(R.id.confirmButton)

        // Get map fragment and request notification when the map is ready to be used
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Handle intent extras if we were provided with a specific location
        val latitude = intent.getDoubleExtra(EXTRA_LATITUDE, 0.0)
        val longitude = intent.getDoubleExtra(EXTRA_LONGITUDE, 0.0)
        if (latitude != 0.0 && longitude != 0.0) {
            selectedLatLng = LatLng(latitude, longitude)
            // We'll update the map when it's ready in onMapReady
        }

        // Set up the confirm button
        confirmButton.setOnClickListener {
            if (selectedLatLng != null) {
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_LATITUDE, selectedLatLng!!.latitude)
                resultIntent.putExtra(EXTRA_LONGITUDE, selectedLatLng!!.longitude)
                resultIntent.putExtra(EXTRA_ADDRESS, selectedAddress)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Silakan pilih lokasi terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Set UI settings
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isCompassEnabled = true
        map.uiSettings.isMyLocationButtonEnabled = true

        // Check if location permissions are granted
        if (checkLocationPermissions()) {
            enableMyLocation()
        }

        // Set up map click listener to allow selecting a location
        map.setOnMapClickListener { latLng ->
            map.clear()
            selectedLatLng = latLng
            addMarkerAtPosition(latLng)
            getAddressFromLocation(latLng)
        }

        // If we already have a selected location (passed from intent), show it
        if (selectedLatLng != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng!!, 15f))
            addMarkerAtPosition(selectedLatLng!!)
            getAddressFromLocation(selectedLatLng!!)
        } else {
            // Otherwise get current location
            getDeviceLocation()
        }
    }

    private fun addMarkerAtPosition(latLng: LatLng) {
        map.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Selected Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )
    }

    private fun checkLocationPermissions(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (fineLocationPermission != PackageManager.PERMISSION_GRANTED ||
            coarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return false
        }
        return true
    }

    private fun enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            getDeviceLocation()
        }
    }

    private fun getDeviceLocation() {
        progressBar.visibility = View.VISIBLE

        try {
            if (checkLocationPermissions()) {
                val locationResult = fusedLocationClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            val currentLatLng = LatLng(
                                lastKnownLocation.latitude,
                                lastKnownLocation.longitude
                            )

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                        } else {
                            // If location is null, use default location (could be Jakarta or another central point)
                            val defaultLocation = LatLng(-6.200000, 106.816666) // Jakarta coordinates
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
                            Toast.makeText(this, "Lokasi saat ini tidak tersedia. Silakan pilih lokasi secara manual.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Tidak dapat mendapatkan lokasi saat ini", Toast.LENGTH_SHORT).show()
                    }
                    progressBar.visibility = View.GONE
                }
            } else {
                progressBar.visibility = View.GONE
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "Error: " + e.message, Toast.LENGTH_SHORT).show()
            progressBar.visibility = View.GONE
        }
    }

    private fun getAddressFromLocation(latLng: LatLng) {
        progressBar.visibility = View.VISIBLE

        try {
            val geocoder = Geocoder(this, Locale.getDefault())

            try {
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    val addressText = buildAddressText(address)
                    addressTextView.text = addressText
                    selectedAddress = addressText
                    confirmButton.isEnabled = true
                } else {
                    addressTextView.text = "No address found"
                    selectedAddress = "Latitude: ${latLng.latitude}, Longitude: ${latLng.longitude}"
                    confirmButton.isEnabled = true
                }
            } catch (e: IOException) {
                addressTextView.text = "Error getting address"
                selectedAddress = "Latitude: ${latLng.latitude}, Longitude: ${latLng.longitude}"
                confirmButton.isEnabled = true
            }
        } catch (e: Exception) {
            addressTextView.text = "Error: ${e.message}"
            selectedAddress = "Latitude: ${latLng.latitude}, Longitude: ${latLng.longitude}"
            confirmButton.isEnabled = true
        }

        progressBar.visibility = View.GONE
    }

    private fun buildAddressText(address: Address): String {
        val sb = StringBuilder()

        // Add thoroughfare (street name)
        if (!address.thoroughfare.isNullOrEmpty()) {
            sb.append(address.thoroughfare)
            sb.append(", ")
        }

        // Add subLocality (neighborhood)
        if (!address.subLocality.isNullOrEmpty()) {
            sb.append(address.subLocality)
            sb.append(", ")
        }

        // Add locality (city)
        if (!address.locality.isNullOrEmpty()) {
            sb.append(address.locality)
            sb.append(", ")
        }

        // Add administrative area (state/province)
        if (!address.adminArea.isNullOrEmpty()) {
            sb.append(address.adminArea)
            sb.append(", ")
        }

        // Add country name
        if (!address.countryName.isNullOrEmpty()) {
            sb.append(address.countryName)
        }

        return sb.toString()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            } else {
                Toast.makeText(this,
                    "Izin lokasi dibutuhkan untuk fitur ini",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
