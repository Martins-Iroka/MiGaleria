package com.martdev.domain.verification

data class UserVerificationDataRequest(
    val code: String,
    val emailID: String
)
