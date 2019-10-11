package com.carlospinan.androidshushme.data.repository

import androidx.lifecycle.LiveData
import com.carlospinan.androidshushme.data.database.ShushDao
import com.carlospinan.androidshushme.data.entities.Place

class ShushRepository(
    private val shushDao: ShushDao
) {

    fun addPlace(place: Place) {
        shushDao.insertPlace(place)
    }

    fun updatePlace(place: Place) {
        shushDao.updatePlace(place)
    }

    fun getAllPlaces(): LiveData<List<Place>> {
        return shushDao.getAllPlaces()
    }

    fun getFilteredPlaces(placeId: Int): LiveData<List<Place>> {
        return shushDao.getFilteredPlaces(placeId)
    }

}