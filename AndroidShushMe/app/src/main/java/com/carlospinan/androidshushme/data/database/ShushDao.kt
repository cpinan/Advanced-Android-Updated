package com.carlospinan.androidshushme.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.carlospinan.androidshushme.data.entities.Place

@Dao
interface ShushDao {

    @Insert
    fun insertPlace(place: Place)

    @Update
    fun updatePlace(place: Place)

    @Query("SELECT * FROM place")
    fun getAllPlaces(): LiveData<List<Place>>

    @Query("SELECT * FROM place WHERE placeId = :placeId")
    fun getFilteredPlaces(placeId: Int): LiveData<List<Place>>

}