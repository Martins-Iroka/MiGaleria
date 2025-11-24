package com.martdev.remote.photo

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val photoRemoteModule = module {
    singleOf(::PhotoRemoteDataSourceImpl) {
        bind<PhotoRemoteDataSource>()
    }
}