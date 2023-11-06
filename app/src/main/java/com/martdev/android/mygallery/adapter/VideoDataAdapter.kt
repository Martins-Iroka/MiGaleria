package com.martdev.android.mygallery.adapter

import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.martdev.android.domain.videomodel.Video

class VideoDataAdapter(private val clickListener: OnClickListener,
private val listener: (Video) -> Unit) :
    ListAdapter<Video, RecyclerView.ViewHolder>(DiffCallback) {

    private var video: Video? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VideoDataHolder.create(parent, clickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VideoDataHolder).bindTo(getItem(position)) {
            video = it
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
                    (holder as VideoDataHolder).setProgressStatus(it)
                }
            }
        }
    }

    fun setProgress(progress: Int) {
        notifyItemChanged(currentList.indexOf(video), Bundle().apply { putInt("progress", progress) })
    }

    private companion object DiffCallback : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem == newItem
        }
    }
}