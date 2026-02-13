package com.martdev.data.resendOTP

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class ResendOTPDataKoinModuleTest {

    @Test
    fun testResendOTPKoinModule() {
        resendOTPDataModule.verify()
    }
}