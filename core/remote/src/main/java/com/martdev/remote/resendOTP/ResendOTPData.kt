package com.martdev.remote.resendOTP

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResendOTPRequest(
    private val email: String
)

@Serializable
data class ResendOTPResponse(
    @SerialName("email_id")
    private val emailId: String
)