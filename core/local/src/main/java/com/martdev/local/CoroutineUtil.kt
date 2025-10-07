package com.martdev.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> doIOOperation(block: suspend () -> T) = withContext(Dispatchers.IO) {
    block()
}