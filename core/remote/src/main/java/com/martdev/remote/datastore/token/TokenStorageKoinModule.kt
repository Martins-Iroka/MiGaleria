package com.martdev.remote.datastore.token

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val tokenModule = module {
    single {
        androidContext().dataStore
    }
    singleOf(::TokenStorageImpl) { bind<TokenStorage>() }
}