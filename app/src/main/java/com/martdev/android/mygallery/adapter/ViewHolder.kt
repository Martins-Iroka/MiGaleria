package com.martdev.android.mygallery.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.mygallery.databinding.PhotoListItemBinding
import com.martdev.android.mygallery.databinding.VideoListItemBinding

class PhotoDataHolder(
    private val binding: PhotoListItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(photo: Photo, listener: (Photo) -> Unit) {
        binding.photoSrc = photo
        binding.downloadPhoto.setOnClickListener {
            listener(photo)
        }
        binding.executePendingBindings()
    }

    fun setProgressStatus(progress: Int) {
        binding.progressBar.progress = progress
        binding.progressText.text = "$progress%"
    }

    companion object {
        fun create(parent: ViewGroup): PhotoDataHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = PhotoListItemBinding.inflate(layoutInflater, parent, false)

            return PhotoDataHolder(binding)
        }
    }
}

class VideoDataHolder(
    private val binding: VideoListItemBinding,
    private val clickListener: OnClickListener
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bindTo(video: Video, listener: (Video) -> Unit) {
        binding.video = video
        binding.playVideo.setOnClickListener {
            clickListener.onClick(video.video_files[0].link)
        }
        binding.downloadVideo.setOnClickListener {
            listener(video)
        }
        binding.executePendingBindings()
    }

    fun setProgressStatus(progress: Int) {
        binding.run {
            progressBar.progress = progress
            progressText.isVisible = progress < 99
            progressText.text = "$progress%"
        }
    }

    companion object {
        fun create(parent: ViewGroup, clickListener: OnClickListener): VideoDataHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = VideoListItemBinding.inflate(layoutInflater, parent, false)

            return VideoDataHolder(binding, clickListener)
        }
    }
}

class OnClickListener(private val clickListener: (String) -> Unit) {
    fun onClick(stringUri: String) = clickListener(stringUri)
}

//class NetworkStateViewHolder(
//    private val binding: NetworkStateViewBinding
//) : RecyclerView.ViewHolder(binding.root) {
//
//    fun <T> bindTo(result: T) {
//        binding.result = result as Result<*>
//        binding.executePendingBindings()
//    }
//
//    companion object {
//        fun create(parent: ViewGroup): NetworkStateViewHolder {
//            val layoutInflater = LayoutInflater.from(parent.context)
//            val binding = NetworkStateViewBinding.inflate(layoutInflater, parent, false)
//
//            return NetworkStateViewHolder(binding)
//        }
//    }
//}