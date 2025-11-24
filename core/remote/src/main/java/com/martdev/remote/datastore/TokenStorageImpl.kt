package com.martdev.remote.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by dataStore(
    fileName = "user-preferences",
    serializer = UserPreferencesSerializer
)

class TokenStorageImpl(
    private val dataStore: DataStore<AuthToken>
) : TokenStorage{

    override fun getTokens() = dataStore.data.map { AuthToken(it.accessToken, it.refreshToken) }

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