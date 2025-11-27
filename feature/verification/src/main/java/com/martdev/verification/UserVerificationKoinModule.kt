package com.martdev.verification

import com.martdev.data.verification.userVerificationDataModule
import com.martdev.domain.verification.userVerificationUseCaseModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val userVerificationModule = module {
    includes(
        userVerificationUseCaseModule,
        userVerificationDataModule
    )
    viewModelOf(::UserVerificationViewModel)
}