package com.martdev.remote.login

import com.martdev.common.NetworkResult
import com.martdev.remote.client.AUTH_LOGIN_PATH
import com.martdev.remote.client.AUTH_LOGOUT_PATH
import com.martdev.remote.client.Client
import com.martdev.remote.ResponseDataPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserLoginRemoteSourceImpl(
    private val client: Client
) : UserLoginRemoteSource {
    override fun loginUser(user: UserLoginRequestPayload): Flow<NetworkResult<ResponseDataPayload<UserLoginResponsePayload>>> {
        return flow {
            val r = client.postData<UserLoginRequestPayload, ResponseDataPayload<UserLoginResponsePayload>>(
                AUTH_LOGIN_PATH, user)

            emit(r)
        }
    }

    override fun logoutUser(user: LogoutUserRequest): Flow<NetworkResult<Unit>> {
        return flow {
            val r = client.postData<LogoutUserRequest, Unit>(
                AUTH_LOGOUT_PATH, user
            )
            emit(r)
        }
    }
}