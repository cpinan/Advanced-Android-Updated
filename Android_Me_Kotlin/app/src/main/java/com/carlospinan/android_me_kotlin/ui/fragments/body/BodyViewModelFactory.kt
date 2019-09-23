package com.carlospinan.android_me_kotlin.ui.fragments.body

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class BodyViewModelFactory(
    private val index: Int,
    private val list: ArrayList<Int>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BodyViewModel::class.java)) {
            return BodyViewModel(index, list) as T
        }
        throw IllegalArgumentException("The $modelClass is not based from BodyViewModel")
    }

}