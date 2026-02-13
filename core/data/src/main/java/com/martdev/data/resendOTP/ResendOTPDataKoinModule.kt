package com.martdev.data.resendOTP

import com.martdev.remote.resendOTP.resendOTPRemoteModule
import org.koin.dsl.module

val resendOTPDataModule = module {
    includes(resendOTPRemoteModule)
    factory {
        resendOTPDataSource(get())
    }
}