package com.martdev.local.database

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val myGalleryKoinModule = module {
    single {
        MyGalleryDB.getInstance(
            androidContext()
        )
    }
}