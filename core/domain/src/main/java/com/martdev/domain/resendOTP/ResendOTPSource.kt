package com.martdev.domain.resendOTP

import com.martdev.domain.ResponseData
import kotlinx.coroutines.flow.Flow

fun interface ResendOTPDataSource {
    fun resendOTP(email: String): Flow<ResponseData<String>>
}