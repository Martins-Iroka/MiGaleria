package com.martdev.login

import com.martdev.data.dataKoinModule
import com.martdev.domain.domainModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val userLoginModule = module {
    includes(domainModule)
    includes(dataKoinModule)
    viewModelOf(::UserLoginViewModel)
}