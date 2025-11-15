package com.martdev.remote

import com.martdev.remote.datastore.AuthToken
import com.martdev.remote.datastore.TokenRefreshRequest
import com.martdev.remote.datastore.TokenRefreshResponse
import com.martdev.remote.datastore.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json

class Client(
    engine: HttpClientEngine,
    private val tokenStorage: TokenStorage
) {

    val httpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
            level = LogLevel.ALL
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val tokens = tokenStorage.getTokens().firstOrNull()
                    if (tokens != null && tokens.accessToken.isNotBlank()) {
                        BearerTokens(tokens.accessToken, tokens.refreshToken)
                    } else {
                        null
                    }
                }

                refreshTokens {
                    val oldToken = tokenStorage.getTokens().firstOrNull()
                    val refreshToken = oldToken?.refreshToken ?: return@refreshTokens null

                    try {
                        val response: TokenRefreshResponse = client.post("/v1/authentication/refresh") {
                            markAsRefreshTokenRequest()
                            contentType(ContentType.Application.Json)
                            setBody(TokenRefreshRequest(refreshToken))
                        }.body()

                        val newTokens = AuthToken(response.accessToken, refreshToken)
                        tokenStorage.saveTokens(newTokens)

                        BearerTokens(newTokens.accessToken, newTokens.refreshToken)
                    } catch (e: Exception) {

                        println("Token refresh failed: ${e.message}. Forcing logout")
                        tokenStorage.clearTokens()

                        null
                    }
                }

                sendWithoutRequest { request ->
                    request.url.pathSegments.contains("register").not() ||
                            request.url.pathSegments.contains("verify").not() ||
                            request.url.pathSegments.contains("login").not() ||
                            request.url.pathSegments.contains("logout").not()
                }
            }
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = BuildConfig.BASE_URL.plus("/v1")
            }
            contentType(ContentType.Application.Json)
            headers {
                this
            }
        }
    }

    suspend inline fun <reified ResponseType> performGetRequest(
        urlString: String,
        crossinline block: HttpRequestBuilder.() -> Unit = {}
    ): ResponseType {
        return httpClient.get(urlString, block).body()
    }

    suspend inline fun <reified BodyType, reified ResponseType> postData(
        urlString: String,
        body: BodyType,
        crossinline block: HttpRequestBuilder.() -> Unit = {}
    ): NetworkResult<ResponseType> {
        return try {
            val response = httpClient.post(urlString) {
                block()
                setBody(body)
            }

            if (response.status.isSuccess()) {
                NetworkResult.Success(response.body())
            } else {
                when(response.status) {
                    HttpStatusCode.BadRequest -> NetworkResult.Failure.BadRequest()
                    HttpStatusCode.Unauthorized -> NetworkResult.Failure.Unauthorized()
                    HttpStatusCode.NotFound -> NetworkResult.Failure.NotFound()
                    else -> NetworkResult.Failure.Other(Exception(response.status.toString()))
                }
            }
        } catch (e: Exception) {
            NetworkResult.Failure.Other(e)
        }
    }
}

sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>
    sealed class Failure(val error: String) : NetworkResult<Nothing> {
        data class BadRequest(val message: String = "Bad Request") : Failure(message)
        data class Unauthorized(val message: String = "Unauthorized") : Failure(message)
        data class NotFound(val message: String = "Not Found") : Failure(message)
        data class Other(val cause: Throwable) : Failure(cause.message.orEmpty().ifEmpty { "An error occurred" })
    }
}