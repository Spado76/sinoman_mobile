package com.example.sinoman

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up clickable UTDI logo
        setupUtdiLogoClickListener()
    }

    private fun setupUtdiLogoClickListener() {
        val utdiLogoContainer: FrameLayout = findViewById(R.id.utdiLogoContainer)

        utdiLogoContainer.setOnClickListener {
            try {
                // Open UTDI website in browser
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://utdi.ac.id/"))
                startActivity(intent)
            } catch (e: Exception) {
                // Handle any errors (like no browser installed)
                Toast.makeText(
                    this,
                    "Tidak dapat membuka website: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
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
