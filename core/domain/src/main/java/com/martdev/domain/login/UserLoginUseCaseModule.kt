package com.martdev.domain.login

import org.koin.dsl.module

val userLoginUseCaseModule = module {
    single {
        UserLoginUseCase(get())
    }
}