package com.martdev.local.photodatasource

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val photoSourceModule = module {
    singleOf(::PhotoDataSourceImpl) {
        bind<PhotoLocalDataSource>()
    }
}