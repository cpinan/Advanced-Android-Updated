package com.carlospinan.androidshushme.data

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.carlospinan.androidshushme.data.entities.ShushPlace
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Result
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

private const val GEOFENCE_TIMEOUT = 60 * 1000 * 60 * 24L // One day
private const val GEOFENCE_RADIUS = 100.0f

class Geofencing(
    private val googleApiClient: GoogleApiClient,
    context: Context
) : ResultCallback<Result> {

    private val geofencePendingIntent: PendingIntent = PendingIntent
        .getBroadcast(
            context,
            0,
            Intent(context, GeofenceBroadcastReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    private val geofenceList = mutableListOf<Geofence>()

    fun registerAllGeofences() {
        if (googleApiClient.isConnected) {
            LocationServices.GeofencingApi
                .addGeofences(
                    googleApiClient,
                    getGeofencingRequest(),
                    geofencePendingIntent
                )
                .setResultCallback(this)
        }
    }

    fun unregisterAllGeofences() {
        if (googleApiClient.isConnected) {
            LocationServices.GeofencingApi
                .removeGeofences(
                    googleApiClient,
                    geofencePendingIntent
                )
                .setResultCallback(this)
        }
    }

    fun updateGeofenceList(places: List<ShushPlace>) {
        geofenceList.clear()
        for (place in places) {
            val placeId = place.placeId
            val placeLatitude = place.latitude
            val placeLongitude = place.longitude

            val geofence = Geofence.Builder()
                .setRequestId(placeId)
                .setExpirationDuration(GEOFENCE_TIMEOUT) // Geofence.NEVER_EXPIRE
                .setCircularRegion(placeLatitude, placeLongitude, GEOFENCE_RADIUS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                .build()
            geofenceList.add(geofence)
        }
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofences(geofenceList)
            .build()
    }

    override fun onResult(result: Result) {
        Log.d("GEOFENCE", "Error adding / removing geofence: ${result.status} ")
    }

}

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("GEOFENCE", "onReceive called")
    }

}