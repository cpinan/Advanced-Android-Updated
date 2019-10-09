package com.carlospinan.androidfirebasemessagingapp.data.repository.database

import com.carlospinan.androidfirebasemessagingapp.R

const val DATABASE_NAME = "squawk.db"

const val TABLE_SQUAWK = "squawk"

// Topic keys as matching what is found in the database
const val ASSER_KEY = R.string.follow_key_switch_asser
const val CEZANNE_KEY = R.string.follow_key_switch_cezanne
const val JLIN_KEY = R.string.follow_key_switch_jlin
const val LYLA_KEY = R.string.follow_key_switch_lyla
const val NIKITA_KEY = R.string.follow_key_switch_nikita
const val TEST_ACCOUNT_KEY = -1

val INSTRUCTOR_KEYS = arrayListOf(
    ASSER_KEY,
    CEZANNE_KEY,
    JLIN_KEY,
    LYLA_KEY,
    NIKITA_KEY
)