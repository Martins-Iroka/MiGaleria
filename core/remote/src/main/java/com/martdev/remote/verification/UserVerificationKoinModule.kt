package com.martdev.remote.verification

import com.martdev.remote.client.clientModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userVerificationRemoteModule = module {
    includes(clientModule)
    singleOf(::UserVerificationRemoteSourceImpl) {
        bind<UserVerificationRemoteSource>()
    }
}