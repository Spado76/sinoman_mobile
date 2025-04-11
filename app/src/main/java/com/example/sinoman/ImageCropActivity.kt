package com.example.sinoman

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.canhub.cropper.CropImageView
import com.canhub.cropper.CropImageOptions
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ImageCropActivity : AppCompatActivity() {

    private lateinit var cropImageView: CropImageView
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_crop)

        // Initialize views
        cropImageView = findViewById(R.id.cropImageView)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)

        // Set up toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel)

        // Get image URI from intent
        val imageUri = intent.getParcelableExtra<Uri>("imageUri")
        if (imageUri != null) {
            cropImageView.setImageUriAsync(imageUri)
        } else {
            finish()
            return
        }

        // Configure crop options
        cropImageView.apply {
            setAspectRatio(1, 1)
            setFixedAspectRatio(true)
            guidelines = CropImageView.Guidelines.ON
            cropShape = CropImageView.CropShape.OVAL
        }

        // Set up button listeners
        saveButton.setOnClickListener {
            val croppedImage = cropImageView.getCroppedImage(500, 500)
            if (croppedImage != null) {
                // Save the cropped image to cache
                val croppedImageUri = saveCroppedImageToCache(croppedImage)
                if (croppedImageUri != null) {
                    val resultIntent = Intent()
                    resultIntent.putExtra("croppedImageUri", croppedImageUri)
                    setResult(RESULT_OK, resultIntent)
                }
            }
            finish()
        }

        cancelButton.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun saveCroppedImageToCache(bitmap: Bitmap): Uri? {
        val cachePath = File(cacheDir, "images")
        cachePath.mkdirs()

        try {
            val file = File(cachePath, "profile_${System.currentTimeMillis()}.jpg")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            stream.close()
            return Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            setResult(RESULT_CANCELED)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
