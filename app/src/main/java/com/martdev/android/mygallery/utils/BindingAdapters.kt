package com.martdev.android.mygallery.utils

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.mygallery.R
import com.martdev.android.mygallery.adapter.PhotoDataAdapter
import com.martdev.android.mygallery.adapter.VideoDataAdapter

@BindingAdapter("photoListData")
fun bindPhotoView(recyclerView: RecyclerView, data: List<Photo>?) {
    val adapter = recyclerView.adapter as PhotoDataAdapter
    data?.let { adapter.submitList(it) }
}

@BindingAdapter("videoListData")
fun bindVideoView(recyclerView: RecyclerView, data: List<Video>?) {
    val adapter = recyclerView.adapter as VideoDataAdapter
    data?.let { adapter.submitList(it) }
}

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imageUrl: String) {
    val imageUri = imageUrl.toUri().buildUpon().scheme("https").build()
    Glide.with(imageView.context)
        .load(imageUri)
        .apply(RequestOptions()
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.no_image))
        .into(imageView)
}
