package com.martdev.remote.util

fun readJsonFile(filePath: String): ByteArray {
    val inputStream = object {}.javaClass.classLoader?.getResourceAsStream(filePath)
        ?: throw IllegalArgumentException("File not found in test resources: $filePath")
    return inputStream.readBytes()
}