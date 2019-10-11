package com.carlospinan.androidshushme.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlospinan.androidshushme.R
import com.carlospinan.androidshushme.ui.dashboard.adapter.PlacesListAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        places_recycler_view.layoutManager = LinearLayoutManager(this)
        places_recycler_view.adapter = PlacesListAdapter()
    }
}
