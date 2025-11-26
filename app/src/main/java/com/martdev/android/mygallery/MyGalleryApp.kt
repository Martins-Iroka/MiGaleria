package com.martdev.android.mygallery

import android.app.Application
import io.kotzilla.sdk.analytics.koin.analytics
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class MyGalleryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyGalleryApp)

            // Add kotzilla analytics
            analytics()
        }
        Timber.plant(Timber.DebugTree())
    }
}