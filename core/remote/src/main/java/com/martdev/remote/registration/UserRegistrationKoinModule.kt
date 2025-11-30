package com.martdev.remote.registration

import com.martdev.remote.client.clientModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userRegistrationRemoteModule = module {
    includes(clientModule)
    singleOf(::UserRegistrationRemoteSourceImpl) { bind<UserRegistrationRemoteSource>() }
}