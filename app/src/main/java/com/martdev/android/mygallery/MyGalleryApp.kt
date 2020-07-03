package com.martdev.android.mygallery

import android.app.Application
import com.martdev.android.data.Injector
import com.martdev.android.data.usecase.UseCase
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.videomodel.Video

class MyGalleryApp : Application() {

    val photoUseCase: UseCase<Photo>
        get() = Injector.providePhotoDataUseCase(this)
    val videoUseCase: UseCase<Video>
        get() = Injector.provideVideoDataUseCase(this)

    override fun onCreate() {
        super.onCreate()
    }
}