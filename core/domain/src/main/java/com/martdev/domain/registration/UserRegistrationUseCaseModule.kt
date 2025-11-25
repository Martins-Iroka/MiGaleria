package com.martdev.domain.registration

import org.koin.dsl.module

internal val userRegistrationUseCaseModule = module {
    single {
        UserRegistrationUseCase(get())
    }
}