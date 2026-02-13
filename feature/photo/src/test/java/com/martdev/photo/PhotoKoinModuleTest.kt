package com.martdev.photo

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import kotlin.test.Test

class PhotoKoinModuleTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun testPhotoKoinModule() {
        photoModule.verify()
    }
}