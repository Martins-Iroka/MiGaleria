package com.martdev.remote.resendOTP

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResendOTPRequestAPI(
    private val email: String
)

@Serializable
data class ResendOTPResponseAPI(
    @SerialName("email_id")
    val emailId: String
)