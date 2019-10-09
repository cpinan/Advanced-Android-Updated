package com.carlospinan.androidfirebasemessagingapp.data.repository.database

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SquawkDao {

    @Insert
    fun insert(squawk: Squawk)

    @Query("SELECT * FROM $TABLE_SQUAWK ORDER BY date DESC")
    fun getSquawks(): LiveData<List<Squawk>>

    @Query("SELECT * FROM $TABLE_SQUAWK WHERE author = :authorName ORDER BY date DESC")
    fun getSquawksFor(authorName: String): LiveData<List<Squawk>>

    @Query("SELECT * FROM $TABLE_SQUAWK WHERE authorKey IN (:authorsKeys) ORDER BY date DESC")
    fun getSquawkFromAuthorKeys(authorsKeys: String): LiveData<List<Squawk>>

}

fun createSelectionForCurrentFollowers(
    resource: Resources,
    preferences: SharedPreferences
): String {
    val builder = StringBuilder()
    for (key in INSTRUCTOR_KEYS) {
        val keyString = resource.getString(key)
        if (preferences.getBoolean(keyString, false)) {
            builder.append("'").append(keyString).append("'").append(",")
        }
    }
    if (builder.isNotEmpty()) {
        builder.deleteCharAt(builder.length - 1)
    }
    return builder.toString()
}