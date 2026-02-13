package com.martdev.data.videosource

import com.martdev.domain.videodata.VideoDataSource
import com.martdev.local.videodatasource.videoLocalSourceKoinModule
import com.martdev.remote.video.videoRemoteModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val videoDataModule = module {
    includes(videoRemoteModule, videoLocalSourceKoinModule)
    singleOf(::VideoDataRepositoryImpl){
        bind<VideoDataSource>()
    }
}