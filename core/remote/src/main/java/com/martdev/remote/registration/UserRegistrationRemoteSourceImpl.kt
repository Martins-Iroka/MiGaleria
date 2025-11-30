package com.martdev.remote.registration

import com.martdev.common.NetworkResult
import com.martdev.remote.client.AUTH_REGISTER_PATH
import com.martdev.remote.client.Client
import com.martdev.remote.ResponseDataPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRegistrationRemoteSourceImpl(
    private val client: Client
) : UserRegistrationRemoteSource {

    override fun registerUser(user: UserRegistrationRequestPayload): Flow<NetworkResult<ResponseDataPayload<UserRegistrationResponsePayload>>> {
        return flow {
            val r = client.postData<UserRegistrationRequestPayload, ResponseDataPayload<UserRegistrationResponsePayload>>(
                AUTH_REGISTER_PATH,
                user
            )

            emit(r)
        }
    }
}