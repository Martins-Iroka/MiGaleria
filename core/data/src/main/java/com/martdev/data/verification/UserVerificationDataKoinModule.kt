package com.martdev.data.verification

import com.martdev.domain.verification.UserVerificationDataSource
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val userVerificationDataModule = module {
    singleOf(::UserVerificationDataSourceImpl) {
        bind<UserVerificationDataSource>()
    }
}