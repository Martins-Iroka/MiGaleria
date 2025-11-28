package com.martdev.data.registration

import com.martdev.data.util.toResponseData
import com.martdev.domain.ResponseData
import com.martdev.domain.registration.UserRegistrationDataRequest
import com.martdev.domain.registration.UserRegistrationDataSource
import com.martdev.remote.datastore.TokenStorage
import com.martdev.remote.registration.UserRegistrationRemoteSource
import com.martdev.remote.registration.UserRegistrationRequestPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class UserRegistrationDataSourceImpl(
    private val remote: UserRegistrationRemoteSource,
    private val tokenStorage: TokenStorage
) : UserRegistrationDataSource {
    override fun registerUser(user: UserRegistrationDataRequest): Flow<ResponseData<Nothing>> {
        return remote.registerUser(UserRegistrationRequestPayload(
            username = user.username,
            email = user.email,
            password = user.password
        )).map {
            it.toResponseData(onSuccess = { response ->
                Timber.e("Token from registration $response")
                tokenStorage.saveVerificationToken(response.data.token)
                null
            })
        }
    }
}