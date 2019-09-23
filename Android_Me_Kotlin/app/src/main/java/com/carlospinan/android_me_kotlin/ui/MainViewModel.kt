package com.carlospinan.android_me_kotlin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _headIndex = MutableLiveData<Int>()
    val headIndex: LiveData<Int>
        get() = _headIndex

    private val _bodyIndex = MutableLiveData<Int>()
    val bodyIndex: LiveData<Int>
        get() = _bodyIndex

    private val _legIndex = MutableLiveData<Int>()
    val legIndex: LiveData<Int>
        get() = _legIndex

    fun updateHeadIndex(index: Int) {
        _headIndex.value = index
    }

    fun updateBodyIndex(index: Int) {
        _bodyIndex.value = index
    }

    fun updateLegIndex(index: Int) {
        _legIndex.value = index
    }

}