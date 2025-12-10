package com.martdev.remote.client

import com.martdev.remote.datastore.token.tokenModule
import com.martdev.remote.datastore.user.userDataStoreKoinModule
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

internal val clientModule = module {
    includes(tokenModule, userDataStoreKoinModule)
    single<HttpClientEngine> {
        OkHttp.create()
    }
    single {
        Client(get(), get())
    }
}