package com.martdev.remote.verification

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userVerificationModule = module {
    singleOf(::UserVerificationRemoteSourceImpl) {
        bind<UserVerificationRemoteSource>()
    }
}