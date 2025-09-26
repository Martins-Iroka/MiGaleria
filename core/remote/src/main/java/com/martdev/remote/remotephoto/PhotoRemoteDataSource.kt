package com.martdev.remote.remotephoto

import com.martdev.remote.Client
import com.martdev.remote.RemoteDataSource
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal const val SEARCH_PHOTO = "v1/search"
internal const val CURATED_PHOTO = "v1/curated"

class PhotoRemoteDataSource(
    private val client: Client
) : RemoteDataSource<PhotoDataAPI> {
    override fun search(query: String): Flow<PhotoDataAPI> {
        return flow {
            val r = client.performGetRequest<PhotoDataAPI>(
                SEARCH_PHOTO, {
                    parameter("query", query)
                }
            )
            emit(r)
        }
    }

    override fun load(): Flow<PhotoDataAPI> {
        return flow {
            val result = client.performGetRequest<PhotoDataAPI>(CURATED_PHOTO)
            emit(result)
        }
    }
}