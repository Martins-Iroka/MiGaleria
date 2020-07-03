package com.martdev.android.mygallery.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.martdev.android.domain.Result
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.mygallery.R
import com.martdev.android.mygallery.viewmodel.PhotoViewModel

class PhotoDataAdapter(private val photoViewModel: PhotoViewModel) :
    PagedListAdapter<Photo, RecyclerView.ViewHolder>(DiffCallback) {

    private var result: Result<List<Photo>>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.photo_list_item -> PhotoDataHolder.create(parent)
            R.layout.network_state_view -> NetworkStateViewHolder.create(parent, photoViewModel)
            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.photo_list_item -> (holder as PhotoDataHolder).bindTo(getItem(position))
            R.layout.network_state_view -> (holder as NetworkStateViewHolder).bindTo(result)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() and (position == itemCount - 1)) R.layout.network_state_view
        else R.layout.photo_list_item
    }

    override fun getItemCount(): Int {
        val extraItem = if (hasExtraRow()) 1 else 0
        return super.getItemCount() + extraItem
    }

    private fun hasExtraRow(): Boolean {
        return (result != null) && (result?.status != Result.Status.SUCCESS)
    }

    fun setNetworkState(result: Result<List<Photo>>) {
        val previousResult = this.result
        val hadExtraRow = hasExtraRow()
        this.result = result
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && (previousResult?.equals(result))!!.not()) {
            notifyItemChanged(itemCount - 1)
        }
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
