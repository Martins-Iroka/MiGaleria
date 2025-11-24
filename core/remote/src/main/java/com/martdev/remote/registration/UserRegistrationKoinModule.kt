package com.martdev.remote.registration

import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userRegistrationModule = module {
    singleOf(::UserRegistrationRemoteSourceImpl) { bind<UserRegistrationRemoteSource>() }
}