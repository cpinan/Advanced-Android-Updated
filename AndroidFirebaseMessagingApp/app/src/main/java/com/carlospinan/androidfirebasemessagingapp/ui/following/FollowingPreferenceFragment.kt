package com.carlospinan.androidfirebasemessagingapp.ui.following

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.google.firebase.messaging.FirebaseMessaging

class FollowingPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val preference = findPreference(key)
        if (preference != null && preference is SwitchPreferenceCompat) {
            // Get the current state of the switch preference
            val isOn = sharedPreferences.getBoolean(key, false)
            if (isOn) {
                // The preference key matches the following key for the associated instructor in
                // FCM. For example, the key for Lyla is key_lyla (as seen in
                // following_squawker.xml). The topic for Lyla's messages is /topics/key_lyla

                // Subscribe
                FirebaseMessaging.getInstance().subscribeToTopic(key)
                //Log.d("", "Subscribing to $key")
            } else {
                // Un-subscribe
                FirebaseMessaging.getInstance().unsubscribeFromTopic(key)
                //Log.d("", "Un-subscribing to $key")
            }
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(com.carlospinan.androidfirebasemessagingapp.R.xml.following_squawker)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Add the shared preference change listener
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the shared preference change listener
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(this)
    }

}