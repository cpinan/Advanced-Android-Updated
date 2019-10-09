package com.carlospinan.androidfirebasemessagingapp.data.repository

import androidx.lifecycle.LiveData
import com.carlospinan.androidfirebasemessagingapp.data.repository.database.Squawk
import com.carlospinan.androidfirebasemessagingapp.data.repository.database.SquawkDao

class SquawkRepository(
    private val dao: SquawkDao
) {

    fun getSquawkFromAuthorKeys(authorKeys: String): LiveData<List<Squawk>> {
        return dao.getSquawkFromAuthorKeys(authorKeys)
    }

}