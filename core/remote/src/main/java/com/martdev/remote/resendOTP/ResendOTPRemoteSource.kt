package com.martdev.remote.resendOTP

import com.martdev.common.NetworkResult
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.client.Client
import com.martdev.remote.client.RESEND_OTP_PATH
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun interface ResendOTPRemoteSource {
    fun resendOTP(request: ResendOTPRequest): Flow<NetworkResult<ResponseDataPayload<ResendOTPResponse>>>
}

fun resendOTPRemoteSourceImpl(client: Client) = ResendOTPRemoteSource {
    flow {
        val r = client.postData<ResendOTPRequest, ResponseDataPayload<ResendOTPResponse>>(
            RESEND_OTP_PATH,
            it
        )
        emit(r)
    }
}