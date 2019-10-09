package com.carlospinan.androidfirebasemessagingapp.ui.following

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.carlospinan.androidfirebasemessagingapp.R

class FollowingPreferenceFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.following_squawker)
    }

}