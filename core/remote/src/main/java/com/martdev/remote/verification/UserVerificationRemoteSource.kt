package com.martdev.remote.verification

import com.martdev.common.NetworkResult
import com.martdev.remote.ResponseDataPayload
import kotlinx.coroutines.flow.Flow

interface UserVerificationRemoteSource {

    fun verifyUser(user: UserVerificationRequestPayload): Flow<NetworkResult<ResponseDataPayload<UserVerificationResponsePayload>>>
}