package com.example.sinoman

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FormPage2Activity : AppCompatActivity() {

    private lateinit var registrationsRecyclerView: RecyclerView
    private lateinit var emptyRegistrationsTextView: TextView
    private lateinit var registrationsAdapter: RegistrationsAdapter
    private var registrations: MutableList<RegistrationData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_page2)

        // Initialize views
        registrationsRecyclerView = findViewById(R.id.registrationsRecyclerView)
        emptyRegistrationsTextView = findViewById(R.id.emptyRegistrationsTextView)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up recycler view
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        loadRegistrations()
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

    private fun showDeleteConfirmationDialog(registration: RegistrationData) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Pendaftaran")
            .setMessage("Apakah Anda yakin ingin menghapus pendaftaran ini?")
            .setPositiveButton("Ya") { _, _ ->
                RegistrationData.deleteRegistration(this, registration.id)

                // Reset submission status in shared preferences if needed
                val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                prefs.edit().putBoolean("has_submitted_application", false).apply()

                // Reset verification status if needed
                prefs.edit().putInt("verification_status", DashboardActivity.STATUS_NOT_REGISTERED).apply()

                loadRegistrations()
                Toast.makeText(this, "Pendaftaran berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Tidak", null)
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            // Navigate to dashboard instead of going back
            val intent = Intent(this, DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    // Override back button to go to dashboard
    override fun onBackPressed() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
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
            RegistrationType.HOUSE_OWNER -> "Pendaftaran Bantuan Rumah"
            RegistrationType.NON_HOUSE_OWNER -> "Pendaftaran Bantuan Rusun"
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
