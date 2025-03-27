package com.example.sinoman

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FormPage2Activity : AppCompatActivity() {

    private lateinit var editPersonalDataButton: Button
    private lateinit var houseOwnerButton: Button
    private lateinit var nonHouseOwnerButton: Button
    private lateinit var registrationsRecyclerView: RecyclerView
    private lateinit var emptyRegistrationsTextView: TextView
    private lateinit var registrationsAdapter: RegistrationsAdapter
    private var registrations: MutableList<RegistrationData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_page2)

        // Initialize views
        editPersonalDataButton = findViewById(R.id.editPersonalDataButton)
        houseOwnerButton = findViewById(R.id.houseOwnerButton)
        nonHouseOwnerButton = findViewById(R.id.nonHouseOwnerButton)
        registrationsRecyclerView = findViewById(R.id.registrationsRecyclerView)
        emptyRegistrationsTextView = findViewById(R.id.emptyRegistrationsTextView)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up recycler view
        setupRecyclerView()

        // Set up button click listeners
        editPersonalDataButton.setOnClickListener {
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }

        houseOwnerButton.setOnClickListener {
            if (RegistrationData.hasActiveRegistrationOfType(this, RegistrationType.NON_HOUSE_OWNER)) {
                Toast.makeText(
                    this,
                    "Anda sudah memiliki pendaftaran bantuan untuk tidak punya rumah. Hapus pendaftaran tersebut terlebih dahulu.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Check if there's an existing in-progress registration
            val existingRegistration = RegistrationData.getInProgressRegistration(this, RegistrationType.HOUSE_OWNER)
            if (existingRegistration != null) {
                // Continue with existing registration
                val intent = Intent(this, FormPage3aActivity::class.java)
                intent.putExtra("registration_id", existingRegistration.id)
                startActivity(intent)
            } else {
                // Create new registration
                val registration = RegistrationData(type = RegistrationType.HOUSE_OWNER)
                RegistrationData.addOrUpdateRegistration(this, registration)

                val intent = Intent(this, FormPage3aActivity::class.java)
                intent.putExtra("registration_id", registration.id)
                startActivity(intent)
            }
        }

        nonHouseOwnerButton.setOnClickListener {
            if (RegistrationData.hasActiveRegistrationOfType(this, RegistrationType.HOUSE_OWNER)) {
                Toast.makeText(
                    this,
                    "Anda sudah memiliki pendaftaran bantuan untuk punya rumah. Hapus pendaftaran tersebut terlebih dahulu.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Check if there's an existing in-progress registration
            val existingRegistration = RegistrationData.getInProgressRegistration(this, RegistrationType.NON_HOUSE_OWNER)
            if (existingRegistration != null) {
                // Continue with existing registration
                val intent = Intent(this, FormPage3bActivity::class.java)
                intent.putExtra("registration_id", existingRegistration.id)
                startActivity(intent)
            } else {
                // Create new registration
                val registration = RegistrationData(type = RegistrationType.NON_HOUSE_OWNER)
                RegistrationData.addOrUpdateRegistration(this, registration)

                val intent = Intent(this, FormPage3bActivity::class.java)
                intent.putExtra("registration_id", registration.id)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadRegistrations()
        updateButtonsState()
    }

    private fun setupRecyclerView() {
        registrationsAdapter = RegistrationsAdapter(
            registrations,
            onDeleteClick = { registration ->
                if (registration.status == RegistrationStatus.IN_PROGRESS) {
                    showDeleteConfirmationDialog(registration)
                } else {
                    Toast.makeText(
                        this,
                        "Pendaftaran yang sudah dikirim tidak dapat dihapus",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        registrationsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@FormPage2Activity)
            adapter = registrationsAdapter
        }
    }

    private fun loadRegistrations() {
        registrations.clear()
        registrations.addAll(RegistrationData.loadRegistrations(this))
        registrationsAdapter.notifyDataSetChanged()

        if (registrations.isEmpty()) {
            emptyRegistrationsTextView.visibility = View.VISIBLE
            registrationsRecyclerView.visibility = View.GONE
        } else {
            emptyRegistrationsTextView.visibility = View.GONE
            registrationsRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun updateButtonsState() {
        val hasHouseOwnerRegistration = RegistrationData.hasActiveRegistrationOfType(this, RegistrationType.HOUSE_OWNER)
        val hasNonHouseOwnerRegistration = RegistrationData.hasActiveRegistrationOfType(this, RegistrationType.NON_HOUSE_OWNER)

        houseOwnerButton.isEnabled = !hasNonHouseOwnerRegistration
        nonHouseOwnerButton.isEnabled = !hasHouseOwnerRegistration
    }

    private fun showDeleteConfirmationDialog(registration: RegistrationData) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Pendaftaran")
            .setMessage("Apakah Anda yakin ingin menghapus pendaftaran ini?")
            .setPositiveButton("Ya") { _, _ ->
                RegistrationData.deleteRegistration(this, registration.id)
                loadRegistrations()
                updateButtonsState()
                Toast.makeText(this, "Pendaftaran berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

class RegistrationsAdapter(
    private val registrations: List<RegistrationData>,
    private val onDeleteClick: (RegistrationData) -> Unit
) : RecyclerView.Adapter<RegistrationsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeTextView: TextView = view.findViewById(R.id.registrationTypeTextView)
        val statusTextView: TextView = view.findViewById(R.id.registrationStatusTextView)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_registration, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val registration = registrations[position]

        // Set registration type
        holder.typeTextView.text = when (registration.type) {
            RegistrationType.HOUSE_OWNER -> "Pendaftaran Bantuan Punya Rumah"
            RegistrationType.NON_HOUSE_OWNER -> "Pendaftaran Bantuan Tidak Punya Rumah"
        }

        // Set registration status
        holder.statusTextView.text = when (registration.status) {
            RegistrationStatus.IN_PROGRESS -> "On Progress (Data Belum Lengkap/Belum Dikirim)"
            RegistrationStatus.UNDER_REVIEW -> "Ditinjau (Data sudah dikirim dan sedang ditinjau)"
            RegistrationStatus.ACCEPTED -> "Diterima"
            RegistrationStatus.REJECTED -> "Ditolak"
        }

        // Set delete button visibility
        if (registration.status == RegistrationStatus.IN_PROGRESS) {
            holder.deleteButton.visibility = View.VISIBLE
            holder.deleteButton.setOnClickListener {
                onDeleteClick(registration)
            }
        } else {
            holder.deleteButton.visibility = View.GONE
        }
    }

    override fun getItemCount() = registrations.size
}

