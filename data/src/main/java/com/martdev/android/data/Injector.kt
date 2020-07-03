package com.martdev.android.data

import android.content.Context
import com.martdev.android.data.repo.PhotoDataRepo
import com.martdev.android.data.repo.VideoDataRepo
import com.martdev.android.data.usecase.PhotoDataUseCase
import com.martdev.android.data.usecase.UseCase
import com.martdev.android.data.usecase.VideoDataUseCase
import com.martdev.android.domain.photomodel.Photo
import com.martdev.android.domain.photomodel.PhotoData
import com.martdev.android.domain.videomodel.Video
import com.martdev.android.domain.videomodel.VideoData
import com.martdev.android.local.LocalDataSource
import com.martdev.android.local.MyGalleryDB
import com.martdev.android.local.PhotoDataSource
import com.martdev.android.local.VideoDataSource
import com.martdev.android.local.entity.*
import com.martdev.android.remote.RemoteDataSource
import com.martdev.android.remote.getApiService
import com.martdev.android.remote.remotephoto.PhotoRemoteDataSource
import com.martdev.android.remote.remotevideo.VideoRemoteDataSource

object Injector {

    private var dataBase: MyGalleryDB? = null

    fun providePhotoDataUseCase(context: Context): UseCase<Photo>{
        dataBase = MyGalleryDB.getInstance(context)

        return PhotoDataUseCase(providePhotoRepo())
    }

    fun provideVideoDataUseCase(context: Context): UseCase<Video> {
        dataBase = MyGalleryDB.getInstance(context)

        return VideoDataUseCase(provideVideoRepo())
    }

    private fun providePhotoRepo(): Repository<Photo>
            = PhotoDataRepo(provideRemotePhotoData(), provideLocalPhotoData())

    private fun provideVideoRepo(): Repository<Video>
            = VideoDataRepo(provideRemoteVideoData(), provideLocalVideoData())

    private fun provideRemotePhotoData(): RemoteDataSource<PhotoData>
            = PhotoRemoteDataSource(getApiService())

    private fun provideLocalPhotoData(): LocalDataSource<PhotoEntity, PhotoDataEntity> {
        val dao = dataBase?.photoData()!!
        val srcDao = dataBase?.photoSrc()!!
        return PhotoDataSource(dao, srcDao)
    }

    private fun provideRemoteVideoData(): RemoteDataSource<VideoData>
            = VideoRemoteDataSource(getApiService())

    private fun provideLocalVideoData(): LocalDataSource<VideoEntity, VideoDataEntity> {
        val dao = dataBase?.videoData()!!
        val userDao = dataBase?.userDao()!!
        val videoFileDao = dataBase?.videoFileDao()!!
        return VideoDataSource(dao, userDao, videoFileDao)
    }
}