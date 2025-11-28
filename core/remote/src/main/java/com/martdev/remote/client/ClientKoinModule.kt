package com.martdev.remote.client

import com.martdev.remote.datastore.tokenModuleModule
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

internal val clientModule = module {
    includes(tokenModuleModule)
    single<HttpClientEngine> {
        OkHttp.create()
    }
    single {
        Client(get(), get())
    }
}