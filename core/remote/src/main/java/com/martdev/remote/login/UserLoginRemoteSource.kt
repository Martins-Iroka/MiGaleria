package com.martdev.remote.login

import com.martdev.common.NetworkResult
import com.martdev.remote.ResponseDataPayload
import kotlinx.coroutines.flow.Flow

interface UserLoginRemoteSource {

    fun loginUser(user: UserLoginRequestPayload): Flow<NetworkResult<ResponseDataPayload<UserLoginResponsePayload>>>

    fun logoutUser(user: LogoutUserRequest): Flow<NetworkResult<Unit>>
}