package com.martdev.android.mygallery

import android.app.Application
import com.martdev.android.data.Injector
import com.martdev.android.domain.usecase.UseCase
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.videomodel.Video
import timber.log.Timber

class MyGalleryApp : Application() {

    /*val photoUseCase: UseCase<Photo>
        get() = Injector.providePhotoDataUseCase()
    val videoUseCase: UseCase<Video>
        get() = Injector.provideVideoDataUseCase()

    override fun onCreate() {
        super.onCreate()
        Injector.provideDatabaseContext(this)
        Timber.plant(Timber.DebugTree())
    }*/
}