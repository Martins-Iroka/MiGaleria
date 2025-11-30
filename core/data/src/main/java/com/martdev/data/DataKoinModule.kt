package com.martdev.data

import com.martdev.data.login.userLoginDataModule
import com.martdev.data.registration.userRegistrationDataModule
import com.martdev.data.verification.userVerificationDataModule
import com.martdev.local.localKoinModule
import org.koin.dsl.module

val dataKoinModule = module {
    includes(
        localKoinModule,
        userLoginDataModule,
        userRegistrationDataModule,
        userVerificationDataModule
        )
}