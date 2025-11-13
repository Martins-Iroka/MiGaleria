package com.martdev.remote.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlinx.coroutines.flow.map

private val Context.dataStore by dataStore(
    fileName = "user-preferences",
    serializer = UserPreferencesSerializer
)

class TokenStorage(
    private val dataStore: DataStore<AuthToken>
) {

    fun getTokens() = dataStore.data.map { BearerTokens(it.accessToken, it.refreshToken) }

    suspend fun saveTokens(token: AuthToken) = dataStore.updateData {
        it.copy(token.accessToken, it.refreshToken)
    }

    suspend fun clearTokens() = dataStore.updateData {
        AuthToken()
    }
}