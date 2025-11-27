package com.martdev.verification

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class UserVerificationKoinModuleTest {

    @Test
    fun testVerificationKoinModule() {
        userVerificationModule.verify()
    }
}