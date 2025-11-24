package com.martdev.remote.datastore

import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val tokenModuleModule = module {
    single {
        androidApplication().dataStore
    }
    singleOf(::TokenStorageImpl) { bind<TokenStorage>() }
}