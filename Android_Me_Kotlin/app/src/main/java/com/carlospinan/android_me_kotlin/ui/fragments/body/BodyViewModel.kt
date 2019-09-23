package com.carlospinan.android_me_kotlin.ui.fragments.body

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BodyViewModel(
    currentIndex: Int,
    private val list: ArrayList<Int>
) : ViewModel() {

    private var _index: Int = currentIndex
    private val _resource = MutableLiveData<Int>()

    val resource: LiveData<Int>
        get() = _resource

    init {
        _resource.value = list[_index]
    }

    fun updateImageResource() {
        _index++
        if (_index > list.size - 1) {
            _index = 0
        }
        _resource.value = list[_index]
    }

}