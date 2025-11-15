package com.martdev.remote.login

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
                "authentication/login", user)

            emit(r)
        }
    }
}