package com.martdev.remote.remotevideo

import com.martdev.remote.Client
import com.martdev.remote.RemoteDataSource
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


const val SEARCH_VIDEO = "videos/search"
const val POPULAR_VIDEO = "videos/popular"

class VideoRemoteDataSource(
    private val client: Client
) : RemoteDataSource<VideoDataAPI> {
    override fun search(query: String): Flow<VideoDataAPI> {
        return flow {
            val result = client.performGetRequest<VideoDataAPI>(SEARCH_VIDEO) {
                parameter("query", query)
            }
            emit(result)
        }
    }

    override fun load(): Flow<VideoDataAPI> {
        return flow {
            val result = client.performGetRequest<VideoDataAPI>(POPULAR_VIDEO)
            emit(result)
        }
    }
}