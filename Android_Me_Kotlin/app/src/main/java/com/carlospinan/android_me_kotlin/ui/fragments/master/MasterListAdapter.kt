package com.carlospinan.android_me_kotlin.ui.fragments.master

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.carlospinan.android_me_kotlin.databinding.ListMasterItemBinding

class MasterListAdapter(
    private val clickListener: MasterClickListener
) : ListAdapter<Int, MasterListAdapter.ViewHolder>(
    DiffCallback
) {

    companion object DiffCallback : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListMasterItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder(
        private val binding: ListMasterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(imageId: Int, clickListener: MasterClickListener) {
            itemView.setOnClickListener {
                clickListener.onClick(adapterPosition)
            }
            binding.androidImageView.setImageResource(imageId)
            binding.executePendingBindings()
        }
    }
}

interface MasterClickListener {
    fun onClick(index: Int)
}