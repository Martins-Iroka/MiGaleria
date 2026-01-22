package com.martdev.common

import org.junit.Test
import kotlin.test.assertEquals

class ConvertToUTCDateTimeTest {

    @Test
    fun testConvertToUTCDateTime() {
        val localTime = convertToUTCDateTime("2026-01-22T18:06:32Z")
        assertEquals("Jan 22, 2026 19:06", localTime)
    }

}