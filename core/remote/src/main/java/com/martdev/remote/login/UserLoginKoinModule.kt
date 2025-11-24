package com.martdev.remote.login

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userLoginModule = module {
    singleOf(::UserLoginRemoteSourceImpl) {
        bind<UserLoginRemoteSource>()
    }
}