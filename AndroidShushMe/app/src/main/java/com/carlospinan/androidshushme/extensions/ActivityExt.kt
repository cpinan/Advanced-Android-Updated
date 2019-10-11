package com.carlospinan.androidshushme.extensions

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun Activity.arePermissionsGranted(permissions: Array<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}

fun Activity.askForPermissions(permissions: Array<String>, permissionCode: Int) {
    ActivityCompat.requestPermissions(
        this,
        permissions,
        permissionCode
    )
}