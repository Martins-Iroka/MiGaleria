package com.martdev.data.resendOTP

import com.martdev.data.util.toResponseData
import com.martdev.domain.ResponseData
import com.martdev.remote.resendOTP.ResendOTPRemoteSource
import com.martdev.remote.resendOTP.ResendOTPRequestAPI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun interface ResendOTPDataSource {
    fun resendOTP(email: String): Flow<ResponseData<String>>
}

fun resendOTPDataSource(remoteResendOTPRemoteSource: ResendOTPRemoteSource) = ResendOTPDataSource { emailID ->
    remoteResendOTPRemoteSource.resendOTP(ResendOTPRequestAPI(emailID)).map { result ->
        result.toResponseData(onSuccess = { otpResponse ->
            otpResponse.data.emailId
        })
    }
}