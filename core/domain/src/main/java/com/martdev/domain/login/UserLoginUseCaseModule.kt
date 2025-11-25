package com.martdev.domain.login

import org.koin.dsl.module

internal val userLoginUseCaseModule = module {
    single {
        UserLoginUseCase(get())
    }
}