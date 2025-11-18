package com.martdev.domain.verification

data class UserVerificationDataRequest(
    private val code: String,
    private val email: String
)
