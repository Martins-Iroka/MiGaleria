package com.martdev.android.mygallery.adapter

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martdev.android.domain.photomodel.Photo

class PhotoDataAdapter(private val listener: (Photo) -> Unit) :
    ListAdapter<Photo, RecyclerView.ViewHolder>(DiffCallback) {

    private var photo: Photo? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoDataHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PhotoDataHolder).bindTo(getItem(position)) {
            photo = it
            listener(it)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.firstOrNull() != null) {
            with(holder.itemView) {
                (payloads.first() as Bundle).getInt("progress").also {
                    (holder as PhotoDataHolder).setProgressStatus(it)
                }
            }
        }
    }

    fun setProgress(progress: Int) {
        notifyItemChanged(currentList.indexOf(photo), Bundle().apply { putInt("progress", progress) })
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }
}
