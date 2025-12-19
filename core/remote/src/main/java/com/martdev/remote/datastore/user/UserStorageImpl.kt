package com.martdev.remote.datastore.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userDataStore by dataStore(
    fileName = "user-preferences.pb",
    serializer = UserDataSerializer
)
class UserStorageImpl(
    private val dataStore: DataStore<UserData>
) : UserStorage {
    override fun getUserData(): Flow<UserData> {
        return dataStore.data.map {
            UserData(
                it.userId
            )
        }
    }

    override suspend fun saveUserId(userID: Long) {
        dataStore.updateData {
            it.copy(userId = userID)
        }
    }

    override suspend fun clearUserData() {
        dataStore.updateData {
            UserData()
        }
    }


}