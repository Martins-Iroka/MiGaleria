package com.martdev.remote.login

import com.martdev.remote.client.clientModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userLoginRemoteModule = module {
    includes(clientModule)
    singleOf(::UserLoginRemoteSourceImpl) {
        bind<UserLoginRemoteSource>()
    }
}