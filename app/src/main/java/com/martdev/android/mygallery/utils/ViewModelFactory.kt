package com.martdev.android.mygallery.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.martdev.android.domain.usecase.UseCase
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.mygallery.viewmodel.PhotoViewModel
import com.martdev.android.mygallery.viewmodel.VideoViewModel

class ViewModelFactory(
    private val photoUseCase: UseCase<Photo>,
    private val videoUseCase: UseCase<Video>
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PhotoViewModel::class.java) -> PhotoViewModel(photoUseCase)
            modelClass.isAssignableFrom(VideoViewModel::class.java) -> VideoViewModel(videoUseCase)
            else -> throw IllegalArgumentException("Unknown model class: $modelClass")
        } as T
    }
}