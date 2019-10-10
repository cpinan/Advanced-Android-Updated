package com.carlospinan.androidfirebasemessagingapp.ui.main

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlospinan.androidfirebasemessagingapp.R
import com.carlospinan.androidfirebasemessagingapp.data.repository.SquawkRepository
import com.carlospinan.androidfirebasemessagingapp.data.repository.database.SquawkDatabase
import com.carlospinan.androidfirebasemessagingapp.ui.following.FollowingPreferenceActivity
import com.carlospinan.androidfirebasemessagingapp.ui.main.adapter.SquawkAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*

const val CHANNEL_ID = "CHANNEL_ID"

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: SquawkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        val viewModelFactory = MainActivityViewModelFactory(
            SquawkRepository(SquawkDatabase.getInstance(this).squawkDao())
        )
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)

        adapter = SquawkAdapter()

        squawks_recycler_view.layoutManager = LinearLayoutManager(this)
        squawks_recycler_view.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        squawks_recycler_view.adapter = adapter

        viewModel.squawk.observe(
            this,
            Observer {
                adapter.submitList(it)
            }
        )

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FBASE", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token

                // Log and toast
                val msg = getString(R.string.msg_token_fmt, token)
                Log.d("FBASE", "MAIN ACT - $msg")
                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_following_preferences -> {
                val intent = Intent(
                    this,
                    FollowingPreferenceActivity::class.java
                )
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
