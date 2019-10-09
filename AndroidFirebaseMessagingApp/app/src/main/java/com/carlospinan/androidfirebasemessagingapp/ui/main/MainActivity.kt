package com.carlospinan.androidfirebasemessagingapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlospinan.androidfirebasemessagingapp.R
import com.carlospinan.androidfirebasemessagingapp.data.repository.SquawkRepository
import com.carlospinan.androidfirebasemessagingapp.data.repository.database.SquawkDatabase
import com.carlospinan.androidfirebasemessagingapp.data.repository.database.createSelectionForCurrentFollowers
import com.carlospinan.androidfirebasemessagingapp.ui.following.FollowingPreferenceActivity
import com.carlospinan.androidfirebasemessagingapp.ui.main.adapter.SquawkAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: SquawkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        viewModel.squawksFromAuthors(
            createSelectionForCurrentFollowers(
                resources,
                PreferenceManager.getDefaultSharedPreferences(this)
            )
        ).observe(
            this,
            Observer {
                adapter.submitList(it)
            }
        )
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
}
