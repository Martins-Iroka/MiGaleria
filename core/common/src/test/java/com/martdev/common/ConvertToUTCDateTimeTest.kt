package com.martdev.common

import org.junit.Test
import java.time.ZoneId
import kotlin.test.assertEquals

class ConvertToUTCDateTimeTest {

    @Test
    fun testConvertToUTCDateTime() {
        val localTime = convertUTCToLocalDateTime("2026-01-22T18:06:32Z", zoneId = ZoneId.of("Africa/Lagos"))
        assertEquals("Jan 22, 2026 19:06", localTime)
    }

}