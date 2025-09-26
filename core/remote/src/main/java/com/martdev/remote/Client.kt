package com.martdev.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class Client(engine: HttpClientEngine) {

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
                host = "api.pexels.com"
                header("Authorization", BuildConfig.PEXELS_API_KEY)
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