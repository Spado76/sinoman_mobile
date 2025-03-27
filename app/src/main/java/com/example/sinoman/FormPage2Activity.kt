package com.example.sinoman

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class FormPage2Activity : AppCompatActivity() {

    private lateinit var hasHouseButton: Button
    private lateinit var noHouseButton: Button
    private lateinit var formData: FormData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_page2)

        // Initialize views
        hasHouseButton = findViewById(R.id.hasHouseButton)
        noHouseButton = findViewById(R.id.noHouseButton)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Load saved form data
        formData = FormData.load(this)

        // Set up button click listeners
        hasHouseButton.setOnClickListener {
            formData.hasHouse = true
            FormData.save(this, formData)
            
            val intent = Intent(this, FormPage3aActivity::class.java)
            startActivity(intent)
        }

        noHouseButton.setOnClickListener {
            formData.hasHouse = false
            FormData.save(this, formData)
            
            val intent = Intent(this, FormPage3bActivity::class.java)
            startActivity(intent)
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

