package com.carlospinan.androidshushme.ui.dashboard

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlospinan.androidshushme.R
import com.carlospinan.androidshushme.extensions.arePermissionsGranted
import com.carlospinan.androidshushme.extensions.askForPermissions
import com.carlospinan.androidshushme.log
import com.carlospinan.androidshushme.ui.dashboard.adapter.PlacesListAdapter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import kotlinx.android.synthetic.main.activity_main.*

/**
 * https://console.cloud.google.com/apis/credentials/key/57becc73-d9cc-4507-b9f3-f25235b2be67?project=theta-signal-186019
 * https://developers.google.com/android/guides/releases
 * https://developers.google.com/places/android-sdk/intro
 * https://developers.google.com/places/android-sdk/reference
 * https://developer.android.com/reference/android/location/Location.html

How do I restrict my API key to specific Android applications?

You can restrict an API key to specific Android applications by providing a debug certificate fingerprint or a release certificate fingerprint.

Debug certificate fingerprint

For Linux or macOS:

$
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

For Windows:

$
keytool -list -v -keystore "%USERPROFILE%\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android

Release certificate fingerprint

$
keytool -list -v -keystore your_keystore_name -alias your_alias_name

Replace your_keystore_name with the fully-qualified path and name of the keystore, including the .keystore extension. Replace your_alias_name with the alias that you assigned to the certificate when you created it.
 */
private const val REQUEST_ACCESS_LOCATION_PERMISSION_CODE = 7777

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ).toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createGoogleApiClient()

        places_recycler_view.layoutManager = LinearLayoutManager(this)
        places_recycler_view.adapter = PlacesListAdapter()

        checkForPermissions()

        location_check_box.setOnClickListener {
            askForPermissions(
                permissions,
                REQUEST_ACCESS_LOCATION_PERMISSION_CODE
            )
        }

        add_location_button.setOnClickListener {
            onAddPlaceButtonClicked()
        }

    }

    private fun createGoogleApiClient() {
        GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .addApi(Places.GEO_DATA_API)
            .enableAutoManage(this, this)
            .build()
    }

    private fun onAddPlaceButtonClicked() {
        if (!arePermissionsGranted(permissions)) {
            Toast.makeText(
                this,
                R.string.need_location_permission_message,
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                this,
                R.string.location_permissions_granted_message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkForPermissions() {
        if (!arePermissionsGranted(permissions)) {
            location_check_box.isEnabled = true
            location_check_box.isChecked = false
        } else {
            location_check_box.isEnabled = false
            location_check_box.isChecked = true
        }
    }

    override fun onConnected(bundle: Bundle?) {
        log("API CONNECTION SUCCESSFUL")
    }

    override fun onConnectionSuspended(state: Int) {
        log("API CONNECTION SUSPENDED")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        log("API CONNECTION FAILED")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_ACCESS_LOCATION_PERMISSION_CODE) {
            checkForPermissions()
        } else {
            Toast.makeText(
                this,
                "PERMISSIONS ARE NOT GRANTED",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
