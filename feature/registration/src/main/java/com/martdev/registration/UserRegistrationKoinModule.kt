package com.martdev.registration

import com.martdev.data.registration.userRegistrationDataModule
import com.martdev.domain.registration.userRegistrationUseCaseModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val userRegistrationModule = module {
    includes(userRegistrationUseCaseModule)
    includes(userRegistrationDataModule)
    viewModelOf(::UserRegistrationViewModel)
}