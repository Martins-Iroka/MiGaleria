package com.martdev.remote.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore by dataStore(
    fileName = "user-preferences",
    serializer = UserPreferencesSerializer
)

class TokenStorageImpl(
    private val dataStore: DataStore<AuthToken>
) : TokenStorage{

    override fun getTokens() = dataStore.data.map { AuthToken(it.accessToken, it.refreshToken) }

    override suspend fun saveTokens(token: AuthToken) {
        dataStore.updateData {
            it.copy(token.accessToken, token.refreshToken)
        }
    }

    override suspend fun clearTokens() {
        dataStore.updateData {
            AuthToken()
        }
    }
}