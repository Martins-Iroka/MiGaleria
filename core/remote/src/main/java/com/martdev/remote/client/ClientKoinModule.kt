package com.martdev.remote.client

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module

internal val clientModule = module {
    single<HttpClientEngine> {
        CIO.create()
    }
    single {
        Client(get(), get())
    }
}