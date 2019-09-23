package com.carlospinan.android_me_kotlin.extensions

import androidx.appcompat.app.AppCompatActivity
import com.carlospinan.android_me_kotlin.R
import com.carlospinan.android_me_kotlin.data.bodies
import com.carlospinan.android_me_kotlin.data.heads
import com.carlospinan.android_me_kotlin.data.legs
import com.carlospinan.android_me_kotlin.ui.fragments.body.BodyPartFragment

fun AppCompatActivity.loadBodyParts(
    headIndex: Int,
    bodyIndex: Int,
    legIndex: Int
) {
    updateHead(headIndex)
    updateBody(bodyIndex)
    updateLeg(legIndex)
}

fun AppCompatActivity.updateHead(index: Int) {
    supportFragmentManager
        .beginTransaction()
        .replace(
            R.id.head_container,
            BodyPartFragment.create(index, heads)
        )
        .commit()
}

fun AppCompatActivity.updateBody(index: Int) {
    supportFragmentManager
        .beginTransaction()
        .replace(
            R.id.body_container,
            BodyPartFragment.create(index, bodies)
        )
        .commit()
}

fun AppCompatActivity.updateLeg(index: Int) {
    supportFragmentManager
        .beginTransaction()
        .replace(
            R.id.leg_container,
            BodyPartFragment.create(index, legs)
        )
        .commit()
}