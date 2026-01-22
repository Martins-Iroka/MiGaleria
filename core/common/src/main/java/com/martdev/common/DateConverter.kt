package com.martdev.common

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun convertToUTCDateTime(utc: String): String {
    val instant = Instant.parse(utc)
    return DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")
        .withZone(ZoneId.systemDefault()).format(instant)
}