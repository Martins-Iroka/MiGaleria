package com.martdev.domain.resendOTP

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val resendOTPUseCaseModule = module {
    factoryOf(::ResendOTPUseCase)
}