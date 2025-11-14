package com.martdev.remote

import com.martdev.remote.datastore.AuthToken
import com.martdev.remote.datastore.TokenRefreshRequest
import com.martdev.remote.datastore.TokenRefreshResponse
import com.martdev.remote.datastore.TokenStorage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpResponseValidator
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
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
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

        HttpResponseValidator {
            validateResponse {
                when(it.status.value) {
                    400 -> throw BadRequestException()
                    401 -> throw UnauthorizedException()
                    404 -> throw NotFoundException()
                    in 500..507 -> throw Exception(it.status.description)
                }
            }
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = BuildConfig.BASE_URL
            }
            contentType(ContentType.Application.Json)
            headers {
                this
            }
        }
    }

    suspend inline fun <reified T> performGetRequest(
        urlString: String,
        crossinline block: HttpRequestBuilder.() -> Unit = {}
    ): T {
        return httpClient.get(urlString, block).body()
    }
}

class BadRequestException(val error: String = "Bad Request") : Exception(error)
class UnauthorizedException(val error: String = "Unauthorized") : Exception(error)
class NotFoundException(val error: String = "Not Found") : Exception(error)