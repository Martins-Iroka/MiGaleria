package com.martdev.remote.remotevideo

import com.martdev.remote.Client
import com.martdev.remote.NetworkResult
import com.martdev.remote.RemoteDataSource
import com.martdev.remote.ResponseDataPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


const val POPULAR_VIDEO = "/videos"

class VideoRemoteDataSource(
    private val client: Client
) : RemoteDataSource<NetworkResult<ResponseDataPayload< List<VideoPostResponse>>>> {

    override fun load(): Flow<NetworkResult<ResponseDataPayload< List<VideoPostResponse>>>> {
        return flow {
            val result = client.getRequest<ResponseDataPayload< List<VideoPostResponse>>>(POPULAR_VIDEO)
            emit(result)
        }
    }
}