package com.martdev.domain.verification

import org.koin.dsl.module

internal val userVerificationUseCaseModule = module {
    single {
        UserVerificationUseCase(get())
    }
}