package com.example.sinoman

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.sinoman.model.GuideSection
import com.example.sinoman.model.GuideStep

class UserGuideActivity : AppCompatActivity() {

    private lateinit var guideSectionsContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_guide)

        // Initialize views
        guideSectionsContainer = findViewById(R.id.guideSectionsContainer)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Load guide sections
        loadGuideSections()
    }

    private fun loadGuideSections() {
        // Create sample guide sections
        val guideSections = createSampleGuideSections()

        // Add sections to container
        for (section in guideSections) {
            addGuideSectionView(section)
        }
    }

    private fun addGuideSectionView(section: GuideSection) {
        val inflater = LayoutInflater.from(this)
        val sectionView = inflater.inflate(R.layout.item_guide_section, guideSectionsContainer, false)

        val sectionIconImageView = sectionView.findViewById<ImageView>(R.id.sectionIconImageView)
        val sectionTitleTextView = sectionView.findViewById<TextView>(R.id.sectionTitleTextView)
        val expandCollapseImageView = sectionView.findViewById<ImageView>(R.id.expandCollapseImageView)
        val sectionHeaderLayout = sectionView.findViewById<LinearLayout>(R.id.sectionHeaderLayout)
        val sectionContentLayout = sectionView.findViewById<LinearLayout>(R.id.sectionContentLayout)

        // Set section data
        sectionIconImageView.setImageResource(section.iconResId)
        sectionTitleTextView.text = section.title

        // Add steps to section content
        for (step in section.steps) {
            val stepView = inflater.inflate(R.layout.item_guide_step, sectionContentLayout, false)
            
            val stepNumberTextView = stepView.findViewById<TextView>(R.id.stepNumberTextView)
            val stepTitleTextView = stepView.findViewById<TextView>(R.id.stepTitleTextView)
            val stepDescriptionTextView = stepView.findViewById<TextView>(R.id.stepDescriptionTextView)
            
            stepNumberTextView.text = step.number.toString()
            stepTitleTextView.text = step.title
            stepDescriptionTextView.text = step.description
            
            sectionContentLayout.addView(stepView)
        }

        // Set initial state
        updateSectionState(section, expandCollapseImageView, sectionContentLayout)

        // Set click listener for expand/collapse
        sectionHeaderLayout.setOnClickListener {
            section.isExpanded = !section.isExpanded
            updateSectionState(section, expandCollapseImageView, sectionContentLayout)
        }

        guideSectionsContainer.addView(sectionView)
    }

    private fun updateSectionState(section: GuideSection, expandCollapseImageView: ImageView, sectionContentLayout: LinearLayout) {
        if (section.isExpanded) {
            expandCollapseImageView.setImageResource(R.drawable.ic_expand_less)
            sectionContentLayout.visibility = View.VISIBLE
            rotateExpandIcon(expandCollapseImageView, 0f, 180f)
        } else {
            expandCollapseImageView.setImageResource(R.drawable.ic_expand_more)
            sectionContentLayout.visibility = View.GONE
            rotateExpandIcon(expandCollapseImageView, 180f, 0f)
        }
    }

    private fun rotateExpandIcon(view: ImageView, from: Float, to: Float) {
        val rotation = ObjectAnimator.ofFloat(view, View.ROTATION, from, to)
        rotation.duration = 300
        rotation.interpolator = AccelerateDecelerateInterpolator()
        rotation.start()
    }

    private fun createSampleGuideSections(): List<GuideSection> {
        return listOf(
            GuideSection(
                id = "upload_document",
                title = "Cara Mendaftar Bantuan Rumah/Rusun",
                iconResId = R.drawable.ic_document,
                steps = listOf(
                    GuideStep(
                        number = 1,
                        title = "Masuk ke menu Pendaftaran",
                        description = "Klik menu \"Pendaftaran\" di navigasi bagian bawah untuk membuka halaman pendaftaran Anda."
                    ),
                    GuideStep(
                        number = 2,
                        title = "Lengkapi formulir",
                        description = "Lengkapi semua formulir dengan data-data Anda."
                    ),
                    GuideStep(
                        number = 3,
                        title = "Unggah dokumen",
                        description = "Pada formulir bagian seperti mengunggah (KTP, KK, Slip Gaji, dll), tekan tombol \"Unggah\"."
                    ),
                    GuideStep(
                        number = 4,
                        title = "Jawab semua pertanyaan",
                        description = "Jawab semua pertanyaan yang tersedia sesua pilihan yang anda pilih (Bantuan Rumah/Bantuan Rusun)."
                    ),
                    GuideStep(
                        number = 5,
                        title = "Klik \"Kirim\"",
                        description = "Setelah dokumen terunggah dan semua field terisi, klik tombol kirim untuk mengirim dokumen ke sistem."
                    )
                )
            ),
            GuideSection(
                id = "check_queue",
                title = "Cara Melihat Posisi Antrean",
                iconResId = R.drawable.ic_time,
                steps = listOf(
                    GuideStep(
                        number = 1,
                        title = "Masuk ke Dashboard",
                        description = "Pada halaman \"Dashboard\", scroll kebagian paling bawah, lalu tekan \"Cek Antrian\"."
                    ),
                    GuideStep(
                        number = 2,
                        title = "Masuk ke web SINOMAN",
                        description = "Klik tombol \"Login\" pada bagian navigasi atas, lalu masuk menggunakan email dan password."
                    ),
                    GuideStep(
                        number = 3,
                        title = "Cek posisi antrean",
                        description = "Pada bagian dashboard utama, posisi antrean Anda akan ditampilkan beserta total skornya."
                    )
                )
            ),
            GuideSection(
                id = "profile_management",
                title = "Mengelola Profil",
                iconResId = R.drawable.ic_person,
                steps = listOf(
                    GuideStep(
                        number = 1,
                        title = "Buka halaman profil",
                        description = "Klik menu \"Profil\" di navigasi bagian bawah."
                    ),
                    GuideStep(
                        number = 2,
                        title = "Edit informasi profil",
                        description = "Klik di kolom yang ingin diubah dan masukkan informasi baru."
                    ),
                    GuideStep(
                        number = 3,
                        title = "Simpan perubahan",
                        description = "Setelah selesai mengedit, klik tombol \"Simpan Perubahan\"."
                    )
                )
            ),
            GuideSection(
                id = "notification_settings",
                title = "Pengaturan Notifikasi",
                iconResId = R.drawable.ic_nav_notifications,
                steps = listOf(
                    GuideStep(
                        number = 1,
                        title = "Buka pengaturan",
                        description = "Klik menu \"Pengaturan\" di sidebar navigasi."
                    ),
                    GuideStep(
                        number = 2,
                        title = "Pilih pengaturan notifikasi",
                        description = "Pilih tab atau menu \"Notifikasi\"."
                    ),
                    GuideStep(
                        number = 3,
                        title = "Sesuaikan preferensi",
                        description = "Aktifkan atau nonaktifkan jenis notifikasi yang ingin Anda terima."
                    ),
                    GuideStep(
                        number = 4,
                        title = "Simpan pengaturan",
                        description = "Klik tombol \"Simpan\" untuk menyimpan preferensi notifikasi Anda."
                    )
                )
            )
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
