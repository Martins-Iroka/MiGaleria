package com.martdev.domain

import com.martdev.domain.login.userLoginUseCaseModule
import com.martdev.domain.registration.userRegistrationUseCaseModule
import com.martdev.domain.verification.userVerificationUseCaseModule
import org.koin.dsl.module

val domainModule = module {
    includes(
        userLoginUseCaseModule,
        userRegistrationUseCaseModule,
        userVerificationUseCaseModule
    )
}