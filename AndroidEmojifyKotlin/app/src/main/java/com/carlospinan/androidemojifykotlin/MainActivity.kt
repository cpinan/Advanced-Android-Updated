package com.carlospinan.androidemojifykotlin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.carlospinan.androidemojifykotlin.extensions.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_IMAGE_CAPTURE = 1
private const val REQUEST_STORAGE_PERMISSION = 1

class MainActivity : AppCompatActivity() {

    private val requiredPermissions = arrayListOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private lateinit var temporalPhotoPath: String
    private lateinit var resultBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emojify_button.setOnClickListener { emojifyMe() }
        save_button.setOnClickListener { saveMe() }
        clear_button.setOnClickListener { clearImage() }
        share_button.setOnClickListener { shareMe() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_STORAGE_PERMISSION -> {
                if (isGranted(grantResults)) {
                    launchCamera()
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        R.string.permission_denied,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            else -> ""
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            processAndSetImage()
        } else {
            deleteImageFile(temporalPhotoPath)
        }
    }

    private fun launchCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            val photoFile = createTempImageFile()
            temporalPhotoPath = photoFile.absolutePath
            val photoUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun processAndSetImage() {

    }

    // CLICK LISTENERS
    private fun clearImage() {

    }

    private fun saveMe() {
        deleteImageFile(temporalPhotoPath)
        saveImage(resultBitmap)
    }

    private fun shareMe() {
        deleteImageFile(temporalPhotoPath)
        saveImage(resultBitmap)
        shareImage(temporalPhotoPath)
    }

    private fun emojifyMe() {
        if (arePermissionGranted(requiredPermissions)) {
            launchCamera()
        } else {
            askForPermissions(requiredPermissions, REQUEST_STORAGE_PERMISSION)
        }
    }

}
