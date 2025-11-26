package com.martdev.registration

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class RegistrationKoinModuleTest {

    @Test
    fun testRegistrationKoinModule() {
        userRegistrationModule.verify()
    }
}