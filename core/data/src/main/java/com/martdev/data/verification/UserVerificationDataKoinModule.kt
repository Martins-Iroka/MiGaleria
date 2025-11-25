package com.martdev.data.verification

import com.martdev.domain.verification.UserVerificationDataSource
import com.martdev.remote.verification.userVerificationModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userVerificationDataModule = module {
    includes(userVerificationModule)
    singleOf(::UserVerificationDataSourceImpl) {
        bind<UserVerificationDataSource>()
    }
}