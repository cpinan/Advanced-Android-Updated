package com.carlospinan.android_me_kotlin.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.carlospinan.android_me_kotlin.R
import com.carlospinan.android_me_kotlin.extensions.loadBodyParts
import com.carlospinan.android_me_kotlin.ui.fragments.master.BODY_INDEX_KEY
import com.carlospinan.android_me_kotlin.ui.fragments.master.HEAD_INDEX_KEY
import com.carlospinan.android_me_kotlin.ui.fragments.master.LEG_INDEX_KEY

class AndroidMeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_android_me)

        if (savedInstanceState == null) {
            intent.extras?.let { extras ->
                loadBodyParts(
                    extras.getInt(HEAD_INDEX_KEY, 0),
                    extras.getInt(BODY_INDEX_KEY, 0),
                    extras.getInt(LEG_INDEX_KEY, 0)

                )
            }

        }
    }
}
