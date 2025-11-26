package com.martdev.domain.login

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val userLoginUseCaseModule = module {
    factoryOf(::UserLoginUseCase)
}