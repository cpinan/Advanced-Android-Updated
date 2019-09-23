package com.carlospinan.android_me_kotlin.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.carlospinan.android_me_kotlin.R
import com.carlospinan.android_me_kotlin.extensions.loadBodyParts
import com.carlospinan.android_me_kotlin.extensions.updateBody
import com.carlospinan.android_me_kotlin.extensions.updateHead
import com.carlospinan.android_me_kotlin.extensions.updateLeg

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (findViewById<View>(R.id.head_container) != null) {
            if (savedInstanceState == null) {
                loadBodyParts(0, 0, 0)
            }
            viewModel.headIndex.observe(
                this,
                Observer { index ->
                    updateHead(index)
                }
            )
            viewModel.bodyIndex.observe(
                this,
                Observer { index ->
                    updateBody(index)
                }
            )
            viewModel.legIndex.observe(
                this,
                Observer { index ->
                    updateLeg(index)
                }
            )
        }
    }

}