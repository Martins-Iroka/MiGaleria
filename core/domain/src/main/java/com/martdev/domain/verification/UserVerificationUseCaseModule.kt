package com.martdev.domain.verification

import org.koin.dsl.module

val userVerificationUseCaseModule = module {
    single {
        UserVerificationUseCase(get())
    }
}