package com.martdev.remote.datastore.user

import androidx.datastore.core.Serializer
import com.martdev.common.Crypto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import java.util.Base64

object UserDataSerializer: Serializer<UserData> {
    override val defaultValue: UserData
        get() = UserData()

    override suspend fun readFrom(input: InputStream): UserData {
        val encryptedInfo = withContext(Dispatchers.IO) {
            input.use { it.readBytes() }
        }

        val decodedBytes = Base64.getDecoder().decode(encryptedInfo)
        val decryptedData = Crypto.decrypt(decodedBytes)
        val decodedJsonString = decryptedData.decodeToString()
        return Json.decodeFromString(decodedJsonString)
    }

    override suspend fun writeTo(
        t: UserData,
        output: OutputStream
    ) {
        val json = Json.encodeToString(t)
        val bytes = json.toByteArray()
        val encryptedBytes = Crypto.encrypt(bytes)
        val base64EncryptedBytes = Base64.getEncoder().encode(encryptedBytes)
        withContext(Dispatchers.IO) {
            output.use {
                it.write(base64EncryptedBytes)
            }
        }
    }
}