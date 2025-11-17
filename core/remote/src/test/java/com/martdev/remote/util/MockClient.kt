package com.martdev.remote.util

import com.martdev.remote.Client
import com.martdev.remote.datastore.TokenStorage
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.assertEquals

fun getMockClient(
    json: String = "",
    path: String = "",
    statusCode: HttpStatusCode = HttpStatusCode.OK,
    tokenStorage: TokenStorage = FakeTokenStorage()
) = Client(
    MockEngine {request ->
        if (path.isNotEmpty()) {
            assertEquals(path, request.url.encodedPath)
        }
        respond(
            content = ByteReadChannel(content = readJsonFile(json)),
            status = statusCode,
            headers = headersOf(HttpHeaders.ContentType, "application/json")
        )
    },
    tokenStorage
)