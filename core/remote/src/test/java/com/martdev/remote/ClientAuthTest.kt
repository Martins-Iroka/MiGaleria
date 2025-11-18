package com.martdev.remote

import com.martdev.remote.datastore.AuthToken
import com.martdev.remote.datastore.TokenStorage
import com.martdev.remote.photo.model.PhotoSrcAPI
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ClientAuthTest {

    @Test
    fun requestSucceedsWhenValidAccessTokenIsPresent() = runTest {
        val mockTokenStorage = mockk<TokenStorage>()
        val mockEngine = MockEngine { request ->
            val authHeader = request.headers[HttpHeaders.Authorization]
            assertEquals("Bearer valid_access_token", authHeader)
            respond(
                content = ByteReadChannel("""{"message":"Success"}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        coEvery { mockTokenStorage.getTokens() } returns flowOf(AuthToken(
            "valid_access_token",
            "valid_refresh_token"
        ))

        val client = Client(mockEngine, mockTokenStorage).httpClient

        val response = client.get("/photos")

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun tokenIsRefreshedAutomaticallyAndOriginalRequestIsRetried() = runTest {
        // 1. Arrange
        var requestCount = 0
        val mockTokenStorage = mockk<TokenStorage>(relaxed = true)
        val mockEngine = MockEngine { request ->
            requestCount++
            when (requestCount) {
                // First attempt to /photos fails with 401
                1 -> {
                    assertEquals(
                        "Bearer expired_access_token",
                        request.headers[HttpHeaders.Authorization]
                    )
                    respondError(HttpStatusCode.Unauthorized)
                }
                // The refresh request to /authentication/refresh
                2 -> {
                    assertEquals("/v1/authentication/refresh", request.url.encodedPath)
                    respond(
                        content = ByteReadChannel("""{"access_token":"new_access_token"}"""),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }
                // The retried request to /photos with the new token
                3 -> {
                    assertEquals(
                        "Bearer new_access_token",
                        request.headers[HttpHeaders.Authorization]
                    )
                    respond(
                        content = ByteReadChannel("""{"message":"Success after refresh"}"""),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json")
                    )
                }

                else -> error("Unexpected request")
            }
        }

        coEvery { mockTokenStorage.getTokens() } returns flowOf(AuthToken(
            "expired_access_token",
            "valid_refresh_token"
        ))

        val client = Client(mockEngine, mockTokenStorage).httpClient

        val response = client.get("/photos")

        assertEquals(HttpStatusCode.OK, response.status)

        coVerify {
            mockTokenStorage.saveTokens(
                AuthToken(
                    "new_access_token",
                    "valid_refresh_token"
                )
            )
        }
    }

    @Test
    fun requestFailsWhenTokenRefreshFails() = runTest {

        val mockTokenStorage = mockk<TokenStorage>(relaxed = true)

        val mockEngine = MockEngine { request ->
            // The first request to the protected endpoint fails
            if (request.url.encodedPath.contains("/photos")) {
                respondError(HttpStatusCode.Unauthorized)
            }
            // The subsequent request to the refresh endpoint also fails
            else if (request.url.encodedPath.contains("/authentication/refresh")) {
                respondError(HttpStatusCode.Unauthorized)
            } else {
                error("Unexpected request to ${request.url.encodedPath}")
            }
        }

        coEvery { mockTokenStorage.getTokens() } returns flowOf(AuthToken(
            "expired_access_token",
            "invalid_refresh_token"
        ))

        val client = Client(mockEngine, mockTokenStorage)
        val r = client.getRequest<ResponseDataPayload<PhotoSrcAPI>>("/photos")
        if (r is NetworkResult.Failure) {
            assertEquals("Unauthorized", r.error)
        }

        coVerify { mockTokenStorage.clearTokens() }
    }

    @Test
    fun noAuthHeaderIsSentForUnprotectedRoutes() = runTest {
        val mockTokenStorage = mockk<TokenStorage>(relaxed = true)
        val mockEngine = MockEngine { request ->
            // Assert the Authorization header is missing
            assertNull(request.headers[HttpHeaders.Authorization])
            respond(
                content = """{"message":"Login page"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = Client(mockEngine, mockTokenStorage).httpClient

        // 2. Act
        val response = client.get("/login")

        // 3. Assert
        assertEquals(HttpStatusCode.OK, response.status)
    }
}