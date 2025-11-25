package com.martdev.local

import com.martdev.local.photodatasource.photoSourceModule
import com.martdev.local.videodatasource.videoSourceKoinModule
import org.koin.dsl.module

val localKoinModule = module {
    includes(
        photoSourceModule,
        videoSourceKoinModule
    )
}