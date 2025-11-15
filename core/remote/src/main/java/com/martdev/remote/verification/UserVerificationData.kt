package com.martdev.remote.verification

data class UserVerificationRequestPayload(
    private val code: String,
    private val email: String,
    private val token: String
)

data class UserVerificationResponsePayload(
    private val status: String
)
