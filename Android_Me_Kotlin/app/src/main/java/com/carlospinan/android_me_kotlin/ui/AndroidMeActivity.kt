package com.carlospinan.android_me_kotlin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carlospinan.android_me_kotlin.databinding.ActivityAndroidMeBinding

class AndroidMeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAndroidMeBinding.inflate(layoutInflater)
    }
}
