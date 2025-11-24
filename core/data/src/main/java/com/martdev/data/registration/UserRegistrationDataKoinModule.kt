package com.martdev.data.registration

import com.martdev.domain.registration.UserRegistrationDataSource
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userRegistrationDataModule = module {
    singleOf(::UserRegistrationDataSourceImpl) {
        bind<UserRegistrationDataSource>()
    }
}