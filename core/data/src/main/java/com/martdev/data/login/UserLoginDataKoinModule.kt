package com.martdev.data.login

import com.martdev.domain.login.UserLoginDataSource
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userLoginDataModule = module {
    singleOf(::UserLoginDataSourceImpl) {
        bind<UserLoginDataSource>()
    }
}