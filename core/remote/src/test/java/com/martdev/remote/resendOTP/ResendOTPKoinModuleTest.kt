package com.martdev.remote.resendOTP

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

class ResendOTPKoinModuleTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun testResendOTPRemoteModule() {
        resendOTPRemoteModule.verify()
    }
}