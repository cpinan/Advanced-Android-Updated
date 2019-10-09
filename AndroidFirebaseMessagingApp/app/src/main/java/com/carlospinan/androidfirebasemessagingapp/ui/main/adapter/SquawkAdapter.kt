package com.carlospinan.androidfirebasemessagingapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlospinan.androidfirebasemessagingapp.R
import com.carlospinan.androidfirebasemessagingapp.data.repository.database.Squawk
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToLong

private const val MINUTE_MILLIS = 1000 * 60
private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
private const val DAY_MILLIS = 24 * HOUR_MILLIS

class SquawkAdapter : ListAdapter<Squawk, SquawkAdapter.ViewHolder>(DiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd MMM")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_squawk_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder) {
            val dateMillis = item.date.toLong()
            val now = System.currentTimeMillis()

            // Change how the date is displayed depending on whether it was written in the last minute,
            // the hour, etc.
            val date = if (now - dateMillis < DAY_MILLIS) {
                if (now - dateMillis < HOUR_MILLIS) {
                    val minutes = ((now - dateMillis) / MINUTE_MILLIS).toDouble().roundToLong()
                    "${minutes}m"
                } else {
                    val minutes = ((now - dateMillis) / HOUR_MILLIS).toDouble().roundToLong()
                    "${minutes}h"
                }
            } else {
                dateFormat.format(Date(dateMillis))
            }

            // Add a dot to the date string

            messageTextView.text = item.message
            authorTextView.text = item.author
            dateTextView.text = "\u2022 $date"
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorImageView: ImageView = itemView.findViewById(R.id.author_image_view)
        val authorTextView: TextView = itemView.findViewById(R.id.author_text_view)
        val dateTextView: TextView = itemView.findViewById(R.id.date_text_view)
        val messageTextView: TextView = itemView.findViewById(R.id.message_text_view)
    }

    class DiffCallback : DiffUtil.ItemCallback<Squawk>() {

        override fun areItemsTheSame(oldItem: Squawk, newItem: Squawk): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Squawk, newItem: Squawk): Boolean {
            return oldItem == newItem
        }

    }

}