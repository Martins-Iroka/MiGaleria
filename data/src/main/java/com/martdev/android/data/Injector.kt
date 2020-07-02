package com.martdev.android.data

import android.content.Context
import com.martdev.android.data.repo.PhotoDataRepo
import com.martdev.android.data.repo.VideoDataRepo
import com.martdev.android.data.usecase.PhotoDataUseCase
import com.martdev.android.data.usecase.VideoDataUseCase
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.photomodel.PhotoData
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.domain.videomodel.VideoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.MyGalleryDB
import com.martdev.android.local.PhotoDataSource
import com.martdev.android.local.VideoDataSource
import com.martdev.android.local.entity.PhotoEntity
import com.martdev.android.local.entity.VideoDataEntity
import com.martdev.android.remote.RemoteDataSource
import com.martdev.android.remote.getApiService
import com.martdev.android.remote.remotephoto.PhotoRemoteDataSource
import com.martdev.android.remote.remotevideo.VideoRemoteDataSource

object Injector {

    private val apiService = getApiService()

    private var dataBase: MyGalleryDB? = null

    fun provideRepos(context: Context, photoUseCase: () -> Unit, videoUseCase: () -> Unit) {
        dataBase = MyGalleryDB.getInstance(context)
        photoUseCase()
        videoUseCase()
    }

    fun providePhotoDataUseCase() = PhotoDataUseCase(providePhotoRepo())

    fun provideVideoDataUseCase() = VideoDataUseCase(provideVideoRepo())

    private fun providePhotoRepo(): Repository<Photo>
            = PhotoDataRepo(provideRemotePhotoData(), provideLocalPhotoData())

    private fun provideVideoRepo(): Repository<Video>
            = VideoDataRepo(provideRemoteVideoData(), provideLocalVideoData())

    private fun provideRemotePhotoData(): RemoteDataSource<PhotoData>
            = PhotoRemoteDataSource(apiService)

    private fun provideLocalPhotoData(): LocalDataSource<PhotoEntity> {
        val dao = dataBase?.photoData()!!
        return PhotoDataSource(dao)
    }

    private fun provideRemoteVideoData(): RemoteDataSource<VideoData>
            = VideoRemoteDataSource(apiService)

    private fun provideLocalVideoData(): LocalDataSource<VideoDataEntity> {
        val dao = dataBase?.videoData()!!
        return VideoDataSource(dao)
    }
}