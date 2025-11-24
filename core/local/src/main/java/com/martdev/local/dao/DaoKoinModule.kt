package com.martdev.local.dao

import com.martdev.local.database.MyGalleryDB
import org.koin.dsl.module

val daoKoinModule = module {
    single {
        val database = get<MyGalleryDB>()
        database.photoDataDao()
    }

    single {
        val database = get<MyGalleryDB>()
        database.videoDataDao()
    }
}