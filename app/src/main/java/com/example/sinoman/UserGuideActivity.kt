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
                title = "Cara Mengunggah Dokumen",
                iconResId = R.drawable.ic_document,
                steps = listOf(
                    GuideStep(
                        number = 1,
                        title = "Masuk ke menu Dokumen",
                        description = "Klik menu \"Dokumen\" di sidebar navigasi untuk membuka halaman dokumen Anda."
                    ),
                    GuideStep(
                        number = 2,
                        title = "Klik tombol \"Unggah Dokumen\"",
                        description = "Anda akan menemukan tombol ini di bagian atas halaman dokumen."
                    ),
                    GuideStep(
                        number = 3,
                        title = "Pilih jenis dokumen",
                        description = "Pilih jenis dokumen yang sesuai dari dropdown menu (KTP, KK, Slip Gaji, dll)."
                    ),
                    GuideStep(
                        number = 4,
                        title = "Unggah file dokumen",
                        description = "Seret file ke area yang disediakan atau klik untuk memilih file dari perangkat Anda.\nFormat yang didukung: PDF, JPG, PNG (maks. 5MB)"
                    ),
                    GuideStep(
                        number = 5,
                        title = "Klik \"Kirim\"",
                        description = "Setelah dokumen terunggah, klik tombol kirim untuk mengirim dokumen ke sistem."
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
                        description = "Klik menu \"Dashboard\" di navigasi utama."
                    ),
                    GuideStep(
                        number = 2,
                        title = "Lihat kartu Status Verifikasi",
                        description = "Pada dashboard, cari kartu dengan judul \"Status Verifikasi\"."
                    ),
                    GuideStep(
                        number = 3,
                        title = "Cek posisi antrean",
                        description = "Posisi antrean Anda akan ditampilkan beserta estimasi waktu tunggu."
                    )
                )
            ),
            GuideSection(
                id = "application_process",
                title = "Proses Pengajuan Bantuan",
                iconResId = R.drawable.ic_document_blue,
                steps = listOf(
                    GuideStep(
                        number = 1,
                        title = "Lengkapi data pribadi",
                        description = "Isi semua data pribadi yang diminta pada form pendaftaran."
                    ),
                    GuideStep(
                        number = 2,
                        title = "Pilih jenis bantuan",
                        description = "Pilih antara \"Bantuan Punya Rumah\" atau \"Bantuan Tidak Punya Rumah\"."
                    ),
                    GuideStep(
                        number = 3,
                        title = "Lengkapi data bantuan",
                        description = "Isi formulir sesuai dengan jenis bantuan yang dipilih."
                    ),
                    GuideStep(
                        number = 4,
                        title = "Unggah dokumen pendukung",
                        description = "Unggah semua dokumen yang diperlukan sesuai petunjuk."
                    ),
                    GuideStep(
                        number = 5,
                        title = "Kirim pengajuan",
                        description = "Tinjau semua informasi dan klik \"Kirim\" untuk mengirimkan pengajuan."
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
                        description = "Klik menu \"Profil Saya\" di sidebar navigasi."
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
