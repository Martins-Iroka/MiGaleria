package com.martdev.android.mygallery

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class AppKoinModuleTest {

    @Test
    fun testAppKoinModule() {
        appModule.verify()
    }
}