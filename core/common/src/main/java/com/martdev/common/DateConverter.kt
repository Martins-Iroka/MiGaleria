package com.martdev.common

import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun convertToUTCDateTime(utc: String): String {
     val dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
        .withZone(ZoneId.systemDefault())
    return ZonedDateTime.parse(utc).format(dtf)
}