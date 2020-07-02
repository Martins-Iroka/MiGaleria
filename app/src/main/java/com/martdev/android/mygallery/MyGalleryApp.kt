package com.martdev.android.mygallery

import android.app.Application
import com.martdev.android.data.Injector
import com.martdev.android.data.usecase.UseCase
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.photomodel.PhotoData
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.domain.videomodel.VideoData

class MyGalleryApp : Application() {

    lateinit var photoUseCase: UseCase<Photo>
    lateinit var videoUseCase: UseCase<Video>

    init {
        Injector.provideRepos(this, {
            photoUseCase = Injector.providePhotoDataUseCase()
        }, {
            videoUseCase = Injector.provideVideoDataUseCase()
        })
    }

}