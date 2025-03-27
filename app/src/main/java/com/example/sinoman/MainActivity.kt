package com.example.sinoman

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var introSlideAdapter: IntroSlideAdapter

    // List of intro slide layouts
    private val introSlideLayouts = listOf(
        R.layout.slide_intro_1,
        R.layout.slide_intro_2,
        R.layout.slide_intro_3
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewPager
        viewPager = findViewById(R.id.viewPager)

        // Set up adapter
        introSlideAdapter = IntroSlideAdapter(introSlideLayouts) { position ->
            Log.d("MainActivity", "Next button clicked at position: $position")
            // Handle next button click
            if (position < introSlideLayouts.size - 1) {
                // If not the last slide, go to next slide
                viewPager.currentItem = position + 1
            } else {
                // If last slide, go to login activity
                try {
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Close MainActivity
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error navigating to LoginActivity", e)
                }
            }
        }

        // Set adapter to ViewPager
        viewPager.adapter = introSlideAdapter
    }
}

