package com.example.sinoman

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.sinoman.model.FaqItem
import com.google.android.material.bottomnavigation.BottomNavigationView

class HelpActivity : AppCompatActivity() {

    private lateinit var emailAddressTextView: TextView
    private lateinit var phoneNumberTextView: TextView
    private lateinit var startChatButton: Button
    private lateinit var viewMapButton: Button
    private lateinit var faqContainer: LinearLayout
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        // Initialize views
        emailAddressTextView = findViewById(R.id.emailAddressTextView)
        startChatButton = findViewById(R.id.startChatButton)
        viewMapButton = findViewById(R.id.viewMapButton)
        faqContainer = findViewById(R.id.faqContainer)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set up email click listener
        emailAddressTextView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:bantuan@sinoman.id")
                putExtra(Intent.EXTRA_SUBJECT, "Bantuan Sinoman")
            }
            startActivity(Intent.createChooser(intent, "Kirim Email"))
        }

        // Set up WhatsApp chat button
        startChatButton.setOnClickListener {
            val phoneNumber = "+6282137191145"
            val url = "https://wa.me/$phoneNumber"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            startActivity(intent)
        }

        // Set up Google Maps button
        viewMapButton.setOnClickListener {
            val mapUrl = "https://maps.app.goo.gl/2gSuoXMRhAdbvNLH9"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(mapUrl)
            }
            startActivity(intent)
        }

        // Set up FAQ section
        setupFaqSection()
    }

    private fun setupFaqSection() {
        val faqItems = createSampleFaqItems()

        // Add FAQ items to container
        for (faqItem in faqItems) {
            addFaqItemView(faqItem)
        }
    }

    private fun addFaqItemView(faqItem: FaqItem) {
        val inflater = LayoutInflater.from(this)
        val faqView = inflater.inflate(R.layout.item_faq, faqContainer, false)

        val questionTextView = faqView.findViewById<TextView>(R.id.faqQuestionTextView)
        val answerTextView = faqView.findViewById<TextView>(R.id.faqAnswerTextView)
        val expandCollapseImageView = faqView.findViewById<ImageView>(R.id.faqExpandCollapseImageView)
        val headerLayout = faqView.findViewById<LinearLayout>(R.id.faqHeaderLayout)

        // Set FAQ data
        questionTextView.text = faqItem.question
        answerTextView.text = faqItem.answer

        // Set initial state
        updateFaqItemState(faqItem, expandCollapseImageView, answerTextView)

        // Set click listener for expand/collapse
        headerLayout.setOnClickListener {
            faqItem.isExpanded = !faqItem.isExpanded
            updateFaqItemState(faqItem, expandCollapseImageView, answerTextView)
        }

        faqContainer.addView(faqView)
    }

    private fun updateFaqItemState(faqItem: FaqItem, expandCollapseImageView: ImageView, answerTextView: TextView) {
        if (faqItem.isExpanded) {
            expandCollapseImageView.setImageResource(R.drawable.ic_expand_less)
            answerTextView.visibility = View.VISIBLE
            rotateExpandIcon(expandCollapseImageView, 0f, 180f)
        } else {
            expandCollapseImageView.setImageResource(R.drawable.ic_expand_more)
            answerTextView.visibility = View.GONE
            rotateExpandIcon(expandCollapseImageView, 180f, 0f)
        }
    }

    private fun rotateExpandIcon(view: ImageView, from: Float, to: Float) {
        val rotation = ObjectAnimator.ofFloat(view, View.ROTATION, from, to)
        rotation.duration = 300
        rotation.interpolator = AccelerateDecelerateInterpolator()
        rotation.start()
    }

    private fun createSampleFaqItems(): List<FaqItem> {
        return listOf(
            FaqItem(
                id = "verification_time",
                question = "Berapa lama waktu verifikasi dokumen?",
                answer = "Proses verifikasi data biasanya membutuhkan waktu 3-5 hari kerja setelah semua dokumen diunggah dengan lengkap."
            ),
            FaqItem(
                id = "queue_position",
                question = "Bagaimana cara mengetahui posisi antrean saya?",
                answer = "Anda dapat melihat posisi antrean di Dashboard aplikasi. Informasi antrean tersedia di kartu 'Status Verifikasi' yang menampilkan posisi dan estimasi waktu tunggu Anda."
            ),
            FaqItem(
                id = "document_formats",
                question = "Format dokumen apa saja yang diterima?",
                answer = "Aplikasi menerima dokumen dalam format PDF, JPG, dan PNG dengan ukuran maksimal 5MB per file."
            ),
            FaqItem(
                id = "application_status",
                question = "Bagaimana cara melihat status pengajuan?",
                answer = "Status pengajuan dapat dilihat di halaman Dashboard pada bagian 'Status Verifikasi'. Status akan diperbarui secara otomatis ketika ada perubahan pada proses pengajuan Anda."
            ),
            FaqItem(
                id = "assistance_types",
                question = "Apa saja jenis bantuan yang tersedia?",
                answer = "Terdapat dua jenis bantuan utama: 'Bantuan Punya Rumah' untuk perbaikan atau renovasi rumah yang sudah dimiliki, dan 'Bantuan Tidak Punya Rumah' untuk bantuan pengadaan rumah baru."
            )
        )
    }

    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.nav_help
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_notifications -> {
                    startActivity(Intent(this, NotificationsActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_form -> {
                    if (FormData.isPersonalDataCompleted(this)) {
                        startActivity(Intent(this, FormPage2Activity::class.java))
                    } else {
                        startActivity(Intent(this, FormActivity::class.java))
                    }
                    finish()
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_help -> true
                else -> false
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
