package com.martdev.remote.photo

import com.martdev.remote.client.clientModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val photoRemoteModule = module {
    includes(clientModule)
    singleOf(::PhotoRemoteDataSourceImpl) {
        bind<PhotoRemoteDataSource>()
    }
}