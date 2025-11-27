package com.martdev.data.verification

import com.martdev.domain.verification.UserVerificationDataSource
import com.martdev.remote.verification.userVerificationRemoteModule
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val userVerificationDataModule = module {
    includes(userVerificationRemoteModule)
    singleOf(::UserVerificationDataSourceImpl) {
        bind<UserVerificationDataSource>()
    }
}