package com.martdev.data.registration

import com.martdev.domain.registration.UserRegistrationDataSource
import com.martdev.remote.registration.userRegistrationRemoteModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userRegistrationDataModule = module {
    includes(userRegistrationRemoteModule)
    singleOf(::UserRegistrationDataSourceImpl) {
        bind<UserRegistrationDataSource>()
    }
}