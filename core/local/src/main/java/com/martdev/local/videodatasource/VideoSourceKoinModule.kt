package com.martdev.local.videodatasource

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val videoSourceKoinModule = module {
    singleOf(::VideoLocalDataSourceImpl) {
        bind<VideoLocalDataSource>()
    }
}