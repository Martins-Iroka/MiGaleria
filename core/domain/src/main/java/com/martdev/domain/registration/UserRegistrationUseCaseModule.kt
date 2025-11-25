package com.martdev.domain.registration

import org.koin.dsl.module

val userRegistrationUseCaseModule = module {
    single {
        UserRegistrationUseCase(get())
    }
}