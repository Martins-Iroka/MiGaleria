package com.martdev.remote.datastore.user

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userDataStoreKoinModule = module {
    single {
        androidContext().userDataStore
    }

    singleOf(::UserStorageImpl) {
        bind<UserStorage>()
    }
}