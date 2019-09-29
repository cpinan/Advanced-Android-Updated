package com.carlospinan.androidemojifykotlin.extensions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun Activity.arePermissionGranted(permissions: ArrayList<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}

fun Activity.askForPermissions(permissions: ArrayList<String>, permissionCode: Int) {
    ActivityCompat.requestPermissions(
        this,
        permissions.toTypedArray(),
        permissionCode
    )
}

fun Activity.isGranted(results: IntArray): Boolean {
    return results.isNotEmpty() && results[0] == PackageManager.PERMISSION_GRANTED
}