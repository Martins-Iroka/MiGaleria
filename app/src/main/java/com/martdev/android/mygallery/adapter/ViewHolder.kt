package com.martdev.android.mygallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.martdev.android.domain.Result
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.mygallery.databinding.NetworkStateViewBinding
import com.martdev.android.mygallery.databinding.PhotoListItemBinding
import com.martdev.android.mygallery.databinding.VideoListItemBinding
import com.martdev.android.mygallery.viewmodel.PhotoViewModel
import com.martdev.android.mygallery.viewmodel.VideoViewModel

class PhotoDataHolder(private val binding: PhotoListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(photo: Photo?) {
        binding.photoSrc = photo
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): PhotoDataHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = PhotoListItemBinding.inflate(layoutInflater, parent, false)

            return PhotoDataHolder(binding)
        }
    }
}

class VideoDataHolder(private val binding: VideoListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindTo(video: Video?) {
        binding.video = video
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): VideoDataHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = VideoListItemBinding.inflate(layoutInflater, parent, false)

            return VideoDataHolder(binding)
        }
    }
}

class NetworkStateViewHolder(
    private val binding: NetworkStateViewBinding,
    private val viewModel: ViewModel
) : RecyclerView.ViewHolder(binding.root) {

    fun <T> bindTo(result: T) {
        binding.result = result as Result<*>
        binding.retryButton.setOnClickListener {
            when(viewModel) {
                is PhotoViewModel -> viewModel.retryQuery()
                is VideoViewModel -> viewModel.retryQuery()
            }
        }
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup, viewModel: ViewModel): NetworkStateViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = NetworkStateViewBinding.inflate(layoutInflater, parent, false)

            return NetworkStateViewHolder(binding, viewModel)
        }
    }
}