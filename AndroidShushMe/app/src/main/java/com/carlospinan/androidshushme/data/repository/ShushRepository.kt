package com.carlospinan.androidshushme.data.repository

import androidx.lifecycle.LiveData
import com.carlospinan.androidshushme.data.database.ShushDao
import com.carlospinan.androidshushme.data.entities.ShushPlace
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ShushRepository(
    private val shushDao: ShushDao
) {

    val allPlaces: LiveData<List<ShushPlace>> = shushDao.getAllPlaces()

    fun addPlace(shushPlace: ShushPlace) {
        GlobalScope.launch(Dispatchers.IO) {
            shushDao.insertPlace(shushPlace)
        }
    }

    fun updatePlace(shushPlace: ShushPlace) {
        GlobalScope.launch(Dispatchers.IO) {
            shushDao.updatePlace(shushPlace)
        }
    }

    fun getFilteredPlaces(placeId: Int): LiveData<List<ShushPlace>> {
        return shushDao.getFilteredPlaces(placeId)
    }

}