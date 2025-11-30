package com.martdev.local.dao

import com.martdev.local.database.MyGalleryDB
import org.koin.dsl.module

internal val photoDaoModule = module {
    single {
        val database = get<MyGalleryDB>()
        database.photoDataDao()
    }
}

internal val videoDaoModule = module {
    single {
        val database = get<MyGalleryDB>()
        database.videoDataDao()
    }
}