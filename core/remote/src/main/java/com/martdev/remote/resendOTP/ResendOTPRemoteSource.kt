package com.martdev.remote.resendOTP

import com.martdev.common.NetworkResult
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.client.Client
import com.martdev.remote.client.RESEND_OTP_PATH
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun interface ResendOTPRemoteSource {
    fun resendOTP(request: ResendOTPRequestAPI): Flow<NetworkResult<ResponseDataPayload<ResendOTPResponseAPI>>>
}

fun resendOTPRemoteSourceImpl(client: Client) = ResendOTPRemoteSource { request ->
    flow {
        emit(client.postData<ResendOTPRequestAPI, ResponseDataPayload<ResendOTPResponseAPI>>(
            RESEND_OTP_PATH,
            request
        ))
    }
}