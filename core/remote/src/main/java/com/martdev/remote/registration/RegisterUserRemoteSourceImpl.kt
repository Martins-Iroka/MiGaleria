package com.martdev.remote.registration

import com.martdev.common.NetworkResult
import com.martdev.remote.AUTH_REGISTER_PATH
import com.martdev.remote.Client
import com.martdev.remote.ResponseDataPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RegisterUserRemoteSourceImpl(
    private val client: Client
) : RegisterUserRemoteSource {

    override fun registerUser(user: RegisterUserRequestPayload): Flow<NetworkResult<ResponseDataPayload<RegisterUserResponsePayload>>> {
        return flow {
            val r = client.postData<RegisterUserRequestPayload, ResponseDataPayload<RegisterUserResponsePayload>>(
                AUTH_REGISTER_PATH,
                user
            )

            emit(r)
        }
    }
}