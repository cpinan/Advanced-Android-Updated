package com.carlospinan.androidshushme.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.carlospinan.androidshushme.data.entities.ShushPlace

@Dao
interface ShushDao {

    @Insert
    fun insertPlace(shushPlace: ShushPlace)

    @Update
    fun updatePlace(shushPlace: ShushPlace)

    @Query("SELECT * FROM place")
    fun getAllPlaces(): LiveData<List<ShushPlace>>

    @Query("SELECT * FROM place WHERE placeId = :placeId")
    fun getFilteredPlaces(placeId: Int): LiveData<List<ShushPlace>>

}