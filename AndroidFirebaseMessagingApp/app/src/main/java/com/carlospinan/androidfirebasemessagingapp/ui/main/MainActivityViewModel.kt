package com.carlospinan.androidfirebasemessagingapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.carlospinan.androidfirebasemessagingapp.data.repository.SquawkRepository
import com.carlospinan.androidfirebasemessagingapp.data.repository.database.Squawk

class MainActivityViewModel(
    private val repository: SquawkRepository
) : ViewModel() {

    val squawk: LiveData<List<Squawk>> = repository.getSquawk()

    fun squawksFromAuthors(authorKeys: String): LiveData<List<Squawk>> {
        return repository.getSquawkFromAuthorKeys(authorKeys)
    }

}