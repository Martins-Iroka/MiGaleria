package com.martdev.remote.registration

import com.martdev.remote.NetworkResult
import com.martdev.remote.ResponseDataPayload
import kotlinx.coroutines.flow.Flow

interface RegisterUserRemoteSource {

    fun registerUser(user: RegisterUserRequestPayload): Flow<NetworkResult<ResponseDataPayload<RegisterUserResponsePayload>>>
}