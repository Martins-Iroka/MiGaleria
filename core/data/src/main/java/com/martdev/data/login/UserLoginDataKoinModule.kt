package com.martdev.data.login

import com.martdev.domain.login.UserLoginDataSource
import com.martdev.remote.login.userLoginRemoteModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userLoginDataModule = module {
    includes(userLoginRemoteModule)
    singleOf(::UserLoginDataSourceImpl) {
        bind<UserLoginDataSource>()
    }
}