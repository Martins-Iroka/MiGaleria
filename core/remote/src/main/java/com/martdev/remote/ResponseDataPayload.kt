package com.martdev.remote

import kotlinx.serialization.Serializable

@Serializable
data class ResponseDataPayload<T>(
    val data: T
)
