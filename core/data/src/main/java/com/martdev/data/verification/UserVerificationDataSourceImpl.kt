package com.martdev.data.verification

import com.martdev.data.util.toResponseData
import com.martdev.domain.ResponseData
import com.martdev.domain.verification.UserVerificationDataRequest
import com.martdev.domain.verification.UserVerificationDataSource
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.datastore.TokenStorage
import com.martdev.remote.verification.UserVerificationRemoteSource
import com.martdev.remote.verification.UserVerificationRequestPayload
import com.martdev.remote.verification.UserVerificationResponsePayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion

class UserVerificationDataSourceImpl(
    private val remote: UserVerificationRemoteSource,
    private val tokenStorage: TokenStorage
) : UserVerificationDataSource {

    override fun verifyUser(user: UserVerificationDataRequest): Flow<ResponseData<Nothing>> {
        return flow {

            val verificationToken = tokenStorage.getTokens().firstOrNull()?.verificationToken
                ?: throw IllegalStateException("No verification token found")

            val r = remote.verifyUser(
                UserVerificationRequestPayload(user.code, user.email, verificationToken)
            ).first()

            emit(r.toResponseData<ResponseDataPayload<UserVerificationResponsePayload>, Nothing>())
        }.onCompletion {
            tokenStorage.clearTokens()
        }
    }
}