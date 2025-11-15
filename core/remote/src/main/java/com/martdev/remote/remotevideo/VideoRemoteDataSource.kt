package com.martdev.remote.remotevideo

import com.martdev.remote.Client
import com.martdev.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


const val POPULAR_VIDEO = "/videos"

class VideoRemoteDataSource(
    private val client: Client
) : RemoteDataSource<VideoPostResponsePayload> {

    override fun load(): Flow<VideoPostResponsePayload> {
        return flow {
            val result = client.performGetRequest<VideoPostResponsePayload>(POPULAR_VIDEO)
            emit(result)
        }
    }
}