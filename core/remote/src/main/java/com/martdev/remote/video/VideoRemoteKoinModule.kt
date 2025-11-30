package com.martdev.remote.video

import com.martdev.remote.client.clientModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val videoRemoteModule = module {
    includes(clientModule)
    singleOf(::VideoRemoteDataSourceImpl) { bind<VideoRemoteDataSource>() }
}