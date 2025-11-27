package com.martdev.login

import com.martdev.data.login.userLoginDataModule
import com.martdev.domain.login.userLoginUseCaseModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val userLoginModule = module {
    includes(userLoginUseCaseModule, userLoginDataModule)
    viewModelOf(::UserLoginViewModel)
}