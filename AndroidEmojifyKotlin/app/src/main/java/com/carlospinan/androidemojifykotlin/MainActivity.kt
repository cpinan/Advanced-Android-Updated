package com.carlospinan.androidemojifykotlin

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.carlospinan.androidemojifykotlin.extensions.*
import com.carlospinan.androidemojifykotlin.utils.Emojifier
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

private const val REQUEST_IMAGE_CAPTURE = 1
private const val REQUEST_STORAGE_PERMISSION = 1

class MainActivity : AppCompatActivity() {

    private val requiredPermissions = arrayListOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private lateinit var temporalPhotoPath: String
    private val resultBitmap = MutableLiveData<Bitmap>()

    private val activityJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + activityJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        emojify_button.setOnClickListener { emojifyMe() }
        save_button.setOnClickListener { saveMe() }
        clear_button.setOnClickListener { clearImage() }
        share_button.setOnClickListener { shareMe() }

        resultBitmap.observe(
            this,
            Observer {
                image_view.setImageBitmap(it)
                progress_bar.visibility = View.GONE

                share_button.show()
                save_button.show()
                clear_button.show()
            }
        )
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
            else -> throw IllegalArgumentException("Invalid Argument.")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            processAndSetImage()
        } else {
            if (::temporalPhotoPath.isInitialized) {
                deleteImageFile(temporalPhotoPath)
            }
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
        emojify_button.visibility = View.GONE
        title_text_view.visibility = View.GONE

        uiScope.launch {
            progress_bar.visibility = View.VISIBLE
            withContext(Dispatchers.IO) {
                Emojifier.detectFaces(
                    this@MainActivity,
                    resamplePic(temporalPhotoPath),
                    resultBitmap
                )
            }
        }

    }

    // CLICK LISTENERS
    private fun clearImage() {
        image_view.setImageResource(0)

        emojify_button.visibility = View.VISIBLE
        title_text_view.visibility = View.VISIBLE

        share_button.hide()
        save_button.hide()
        clear_button.hide()
    }

    private fun saveMe() {
        if (::temporalPhotoPath.isInitialized) {
            deleteImageFile(temporalPhotoPath)
            saveImage(resultBitmap.value!!)
        }
    }

    private fun shareMe() {
        if (::temporalPhotoPath.isInitialized) {
            deleteImageFile(temporalPhotoPath)
            saveImage(resultBitmap.value!!)
            shareImage(temporalPhotoPath)
        }
    }

    private fun emojifyMe() {
        if (arePermissionGranted(requiredPermissions)) {
            launchCamera()
        } else {
            askForPermissions(requiredPermissions, REQUEST_STORAGE_PERMISSION)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        activityJob.cancel()
    }

}
