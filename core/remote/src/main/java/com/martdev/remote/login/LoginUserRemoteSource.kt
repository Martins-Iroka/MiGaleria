package com.martdev.remote.login

import com.martdev.common.NetworkResult
import com.martdev.remote.ResponseDataPayload
import kotlinx.coroutines.flow.Flow

interface LoginUserRemoteSource {

    fun loginUser(user: LoginUserRequestPayload): Flow<NetworkResult<ResponseDataPayload<LoginUserResponsePayload>>>

    fun logoutUser(user: LogoutUserRequest): Flow<NetworkResult<Unit>>
}