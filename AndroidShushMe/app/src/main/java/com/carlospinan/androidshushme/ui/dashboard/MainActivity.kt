package com.carlospinan.androidshushme.ui.dashboard

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlospinan.androidshushme.R
import com.carlospinan.androidshushme.data.Geofencing
import com.carlospinan.androidshushme.data.database.ShushDatabase
import com.carlospinan.androidshushme.data.entities.ShushPlace
import com.carlospinan.androidshushme.data.repository.ShushRepository
import com.carlospinan.androidshushme.extensions.arePermissionsGranted
import com.carlospinan.androidshushme.extensions.askForPermissions
import com.carlospinan.androidshushme.log
import com.carlospinan.androidshushme.ui.dashboard.adapter.PlacesListAdapter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker
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
private const val REQUEST_PLACE_PICKER_CODE = 7778

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    private val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ).toTypedArray()

    private var switchEnabled = false
    private lateinit var adapter: PlacesListAdapter
    private lateinit var repository: ShushRepository
    private lateinit var geofencing: Geofencing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createGoogleApiClient()

        repository = ShushRepository(
            ShushDatabase.getInstance(this).shushDao()
        )
        adapter = PlacesListAdapter()
        places_recycler_view.layoutManager = LinearLayoutManager(this)
        places_recycler_view.adapter = adapter

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

        switchEnabled = getPreferences(Context.MODE_PRIVATE).getBoolean(
            getString(R.string.setting_enabled),
            false
        )
        enable_switch.isChecked = switchEnabled
        enable_switch.setOnCheckedChangeListener { _, isChecked ->
            val editor = getPreferences(Context.MODE_PRIVATE).edit()
            editor.putBoolean(getString(R.string.setting_enabled), isChecked)
            switchEnabled = isChecked
            editor.commit()
            if (switchEnabled) {
                geofencing.registerAllGeofences()
            } else {
                geofencing.unregisterAllGeofences()
            }
        }

        repository.allPlaces.observe(
            this,
            Observer {
                adapter.submitList(it)
                geofencing.updateGeofenceList(it)
                if (switchEnabled) {
                    geofencing.registerAllGeofences()
                }
            }
        )

    }

    private fun createGoogleApiClient() {
        val googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .addApi(Places.GEO_DATA_API)
            .enableAutoManage(this, this)
            .build()

        geofencing = Geofencing(
            googleApiClient,
            this
        )
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

            val builder = PlacePicker.IntentBuilder()
            val intent = builder.build(this)
            startActivityForResult(intent, REQUEST_PLACE_PICKER_CODE)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_PLACE_PICKER_CODE) {
            data?.let {
                val place: Place? = PlacePicker.getPlace(this, it)
                if (place != null) {
                    repository.addPlace(
                        ShushPlace(
                            id = 0,
                            name = place.name.toString(),
                            latitude = place.latLng.latitude,
                            longitude = place.latLng.longitude,
                            placeId = place.id,
                            address = place.address.toString()
                        )
                    )
                } else {
                    log("NO PLACE SELECTED")
                }
            }
        }
    }


}
