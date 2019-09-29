package com.carlospinan.androidemojifykotlin.extensions

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min

const val FILE_PROVIDER_AUTHORITY = "com.carlospinan.fileprovider"

/**
 * Resamples the captured photo to fit the screen for better memory usage.
 *
 * @param imagePath The path of the photo to be resampled.
 * @return The resampled bitmap
 */
fun Context.resamplePic(imagePath: String): Bitmap {
    // Get device screen size information
    val metrics = DisplayMetrics()
    val manager = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    manager.defaultDisplay.getMetrics(metrics)

    val targetH = metrics.heightPixels
    val targetW = metrics.widthPixels

    // Get the dimensions of the original bitmap
    val bmOptions = BitmapFactory.Options()
    bmOptions.inJustDecodeBounds = true
    BitmapFactory.decodeFile(imagePath, bmOptions)
    val photoW = bmOptions.outWidth
    val photoH = bmOptions.outHeight

    // Determine how much to scale down the image
    val scaleFactor = min(photoW / targetW, photoH / targetH)

    // Decode the image file into a Bitmap sized to fill the View
    bmOptions.inJustDecodeBounds = false
    bmOptions.inSampleSize = scaleFactor

    return BitmapFactory.decodeFile(imagePath)
}

/**
 * Creates the temporary image file in the cache directory.
 *
 * @return The temporary image file.
 * @throws IOException Thrown if there is an error creating the file
 */
@Throws(IOException::class)
fun Context.createTempImageFile(): File {
    val timeStamp = SimpleDateFormat(
        "yyyyMMdd_HHmmss",
        Locale.getDefault()
    ).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = this.externalCacheDir

    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        storageDir      /* directory */
    )
}

/**
 * Deletes image file for a given path.
 *
 * @param imagePath The path of the photo to be deleted.
 */
fun Context.deleteImageFile(imagePath: String): Boolean {
    // Get the file
    val imageFile = File(imagePath)

    // Delete the image
    val deleted = imageFile.delete()

    // If there is an error deleting the file, show a Toast
    if (!deleted) {
        val errorMessage = this.getString(com.carlospinan.androidemojifykotlin.R.string.error)
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    return deleted
}

/**
 * Helper method for adding the photo to the system photo gallery so it can be accessed
 * from other apps.
 *
 * @param imagePath The path of the saved image
 */
fun Context.galleryAddPic(imagePath: String) {
    val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
    val f = File(imagePath)
    val contentUri = Uri.fromFile(f)
    mediaScanIntent.data = contentUri
    this.sendBroadcast(mediaScanIntent)
}

/**
 * Helper method for saving the image.
 *
 * @param image   The image to be saved.
 * @return The path of the saved image.
 */
fun Context.saveImage(image: Bitmap): String? {

    var savedImagePath: String? = null

    // Create the new file in the external storage
    val timeStamp = SimpleDateFormat(
        "yyyyMMdd_HHmmss",
        Locale.getDefault()
    ).format(Date())
    val imageFileName = "JPEG_$timeStamp.jpg"

    val storageDir = File(
        "${this.getExternalFilesDirs(Environment.DIRECTORY_PICTURES)}/Emojify"

    )
    var success = true
    if (!storageDir.exists()) {
        success = storageDir.mkdirs()
    }

    // Save the new Bitmap
    if (success) {
        val imageFile = File(storageDir, imageFileName)
        savedImagePath = imageFile.absolutePath
        try {
            val fOut = FileOutputStream(imageFile)
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
            fOut.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Add the image to the system gallery
        this.galleryAddPic(savedImagePath)

        // Show a Toast with the save location
        val savedMessage = this.getString(
            com.carlospinan.androidemojifykotlin.R.string.saved_message,
            savedImagePath
        )
        Toast.makeText(this, savedMessage, Toast.LENGTH_SHORT).show()
    }

    return savedImagePath
}

/**
 * Helper method for sharing an image.
 *
 * @param imagePath The path of the image to be shared.
 */
fun Context.shareImage(imagePath: String) {
    // Create the share intent and start the share activity
    val imageFile = File(imagePath)
    val shareIntent = Intent(Intent.ACTION_SEND)
    shareIntent.type = "image/*"
    val photoURI = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, imageFile)
    shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI)
    this.startActivity(shareIntent)
}