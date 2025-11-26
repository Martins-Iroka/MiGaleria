package com.martdev.domain.verification

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val userVerificationUseCaseModule = module {
    factoryOf(::UserVerificationUseCase)
}