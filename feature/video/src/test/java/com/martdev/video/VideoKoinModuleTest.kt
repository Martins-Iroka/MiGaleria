package com.martdev.video

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import kotlin.test.Test

class VideoKoinModuleTest {

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun testVideoKoinModule() {
        videoModule.verify()
    }
}