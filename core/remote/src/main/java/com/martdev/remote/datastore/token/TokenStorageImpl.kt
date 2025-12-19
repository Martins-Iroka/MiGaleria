package com.martdev.remote.datastore.token

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by dataStore(
    fileName = "token-preferences.pb",
    serializer = AuthTokenSerializer
)

class TokenStorageImpl(
    context: Context
) : TokenStorage{

    val dataStore: DataStore<AuthToken> = context.dataStore
    override fun getTokens() = dataStore.data.map { AuthToken(
        accessToken = it.accessToken,
        refreshToken = it.refreshToken,
        verificationToken = it.verificationToken
    ) }

    override suspend fun saveAuthTokens(token: AuthToken) {
        dataStore.updateData {
            it.copy(accessToken = token.accessToken, refreshToken = token.refreshToken)
        }
    }

    override suspend fun saveVerificationToken(token: String) {
        dataStore.updateData {
            it.copy(verificationToken = token)
        }
    }

    override suspend fun clearTokens() {
        dataStore.updateData {
            AuthToken()
        }
    }
}