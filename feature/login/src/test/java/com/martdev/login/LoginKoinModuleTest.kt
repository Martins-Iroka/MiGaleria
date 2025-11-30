package com.martdev.login

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class LoginKoinModuleTest {

    @Test
    fun testLoginKoinModule() {
        userLoginModule.verify()
    }
}