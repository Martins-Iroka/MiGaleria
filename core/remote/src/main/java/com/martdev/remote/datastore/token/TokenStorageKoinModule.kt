package com.martdev.remote.datastore.token

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal val tokenModule = module {
    single<TokenStorage> {
        TokenStorageImpl(androidContext())
    }
}