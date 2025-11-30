package com.martdev.local

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class LocalKoinModuleTest {

    @Test
    fun testLocalKoinModule() {
        localKoinModule.verify()
    }
}