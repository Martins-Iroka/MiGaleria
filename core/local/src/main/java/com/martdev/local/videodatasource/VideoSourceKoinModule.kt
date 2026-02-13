package com.martdev.local.videodatasource

import com.martdev.local.dao.myGalleryDB
import com.martdev.local.dao.videoDaoModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val videoLocalSourceKoinModule = module {
    includes(videoDaoModule, myGalleryDB)
    singleOf(::VideoLocalDataSourceImpl) {
        bind<VideoLocalDataSource>()
    }
}