package com.martdev.remote.registration

import com.martdev.common.NetworkResult
import com.martdev.remote.ResponseDataPayload
import kotlinx.coroutines.flow.Flow

interface UserRegistrationRemoteSource {

    fun registerUser(user: UserRegistrationRequestPayload): Flow<NetworkResult<ResponseDataPayload<UserRegistrationResponsePayload>>>
}