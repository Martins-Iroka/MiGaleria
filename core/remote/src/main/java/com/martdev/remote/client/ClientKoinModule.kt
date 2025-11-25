package com.martdev.remote.client

import com.martdev.remote.datastore.tokenModuleModule
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import org.koin.dsl.module

internal val clientModule = module {
    includes(tokenModuleModule)
    single<HttpClientEngine> {
        CIO.create()
    }
    single {
        Client(get(), get())
    }
}