package com.carlospinan.androidshushme.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlospinan.androidshushme.R
import com.carlospinan.androidshushme.data.entities.ShushPlace

class PlacesDiffCallback : DiffUtil.ItemCallback<ShushPlace>() {

    override fun areItemsTheSame(oldItem: ShushPlace, newItem: ShushPlace): Boolean {
        return oldItem.placeId == newItem.placeId
    }

    override fun areContentsTheSame(oldItem: ShushPlace, newItem: ShushPlace): Boolean {
        return oldItem == newItem
    }

}

class PlacesListAdapter : ListAdapter<ShushPlace, PlacesListAdapter.ViewHolder>(PlacesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_place_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder) {
            nameTextView.text = item.placeId.toString()
            addressTextView.text = item.placeId.toString()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name_text_view)
        val addressTextView: TextView = itemView.findViewById(R.id.address_text_view)
    }

}