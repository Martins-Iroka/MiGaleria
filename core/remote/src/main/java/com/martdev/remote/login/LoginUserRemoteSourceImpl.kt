package com.martdev.remote.login

import com.martdev.remote.AUTH_LOGIN_PATH
import com.martdev.remote.AUTH_LOGOUT_PATH
import com.martdev.remote.Client
import com.martdev.remote.NetworkResult
import com.martdev.remote.ResponseDataPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginUserRemoteSourceImpl(
    private val client: Client
) : LoginUserRemoteSource {
    override fun loginUser(user: LoginUserRequestPayload): Flow<NetworkResult<ResponseDataPayload<LoginUserResponsePayload>>> {
        return flow {
            val r = client.postData<LoginUserRequestPayload, ResponseDataPayload<LoginUserResponsePayload>>(
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