package com.martdev.local

suspend fun <T> doIOOperation(block: suspend () -> T) = block()