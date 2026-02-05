package com.martdev.remote.resendOTP

import com.martdev.remote.client.clientModule
import org.koin.dsl.module

val resendOTPRemoteModule = module {
    includes(clientModule)
    factory<ResendOTPRemoteSource> {
        resendOTPRemoteSourceImpl(get())
    }
}