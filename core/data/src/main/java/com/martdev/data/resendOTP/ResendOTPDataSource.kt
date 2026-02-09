package com.martdev.data.resendOTP

import com.martdev.data.util.toResponseData
import com.martdev.domain.resendOTP.ResendOTPDataSource
import com.martdev.remote.resendOTP.ResendOTPRemoteSource
import com.martdev.remote.resendOTP.ResendOTPRequestAPI
import kotlinx.coroutines.flow.map

fun resendOTPDataSource(remoteResendOTPRemoteSource: ResendOTPRemoteSource) =
    ResendOTPDataSource { emailID ->
        remoteResendOTPRemoteSource.resendOTP(ResendOTPRequestAPI(emailID)).map { result ->
            result.toResponseData(onSuccess = { otpResponse ->
                otpResponse.data.emailId
            })
        }
    }