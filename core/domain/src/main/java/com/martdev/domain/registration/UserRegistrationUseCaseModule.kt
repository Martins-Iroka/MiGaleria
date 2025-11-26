package com.martdev.domain.registration

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val userRegistrationUseCaseModule = module {
    factoryOf(::UserRegistrationUseCase)
}