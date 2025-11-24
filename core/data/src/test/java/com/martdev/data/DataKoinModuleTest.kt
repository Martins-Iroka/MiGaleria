package com.martdev.data

import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

@OptIn(KoinExperimentalAPI::class)
class DataKoinModuleTest {

    @Test
    fun testDataKoinModules() {
        dataKoinModule.verify()
    }
}