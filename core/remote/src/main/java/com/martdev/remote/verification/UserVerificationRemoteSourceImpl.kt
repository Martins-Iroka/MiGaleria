package com.martdev.remote.verification

import com.martdev.remote.Client
import com.martdev.remote.NetworkResult
import com.martdev.remote.ResponseDataPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserVerificationRemoteSourceImpl(
    private val client: Client
) : UserVerificationRemoteSource {
    override fun verifyUser(user: UserVerificationRequestPayload): Flow<NetworkResult<ResponseDataPayload<UserVerificationResponsePayload>>> {
        return flow {
            val r = client.postData<UserVerificationRequestPayload, ResponseDataPayload<UserVerificationResponsePayload>>(
                "authentication/verify",
                user
            )

            emit(r)
        }
    }
}