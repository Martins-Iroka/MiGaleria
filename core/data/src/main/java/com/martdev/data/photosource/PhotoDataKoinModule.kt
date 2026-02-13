package com.martdev.data.photosource

import com.martdev.domain.photodata.PhotoDataSource
import com.martdev.local.photodatasource.photoLocalSourceModule
import com.martdev.remote.photo.photoRemoteModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val photoDataModule = module {
    includes(photoRemoteModule, photoLocalSourceModule)
    singleOf(::PhotoDataRepositoryImpl) {
        bind<PhotoDataSource>()
    }
}