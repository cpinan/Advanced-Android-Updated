package com.carlospinan.androidshushme

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest

class TestNewPlacesApi : AppCompatActivity() {

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // TEST PLACE 2.0
        // https://developers.google.com/places/android-sdk/place-id
        fun test() {
            // Define a Place ID.
            val placeId = "INSERT_PLACE_ID_HERE"

            // Specify the fields to return.
            val placeFields = listOf(Place.Field.ID, Place.Field.NAME)

            // Construct a request object, passing the place ID and fields array.
            val request = FetchPlaceRequest.builder(placeId, placeFields).build()

            // Initialize the SDK
            Places.initialize(
                applicationContext, resources.getString(
                    R.string.google_maps_api_key
                )
            )

            // Create a new Places client instance
            val placesClient = Places.createClient(this)


            placesClient.fetchPlace(request)
                .addOnSuccessListener { response ->
                    val place = response.place
                    log("Place found ${place.name}")
                }
                .addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        log("Place not found ${exception.message}")
                    }
                }
        }
    }

}