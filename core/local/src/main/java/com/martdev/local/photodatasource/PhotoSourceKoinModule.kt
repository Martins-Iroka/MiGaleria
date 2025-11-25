package com.martdev.local.photodatasource

import com.martdev.local.dao.photoDaoModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val photoSourceModule = module {
    includes(photoDaoModule)
    singleOf(::PhotoDataSourceImpl) {
        bind<PhotoLocalDataSource>()
    }
}