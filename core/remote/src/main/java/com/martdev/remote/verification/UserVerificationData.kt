package com.martdev.remote.verification

import kotlinx.serialization.Serializable

@Serializable
data class UserVerificationRequestPayload(
    private val code: String,
    private val email: String,
    private val token: String
)

@Serializable
data class UserVerificationResponsePayload(
    val status: String
)
