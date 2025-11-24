package com.martdev.remote.video

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val videoRemoteModule = module {
    singleOf(::VideoRemoteDataSourceImpl) { bind<VideoRemoteDataSource>() }
}