package com.martdev.remote.video

import com.martdev.remote.Client
import com.martdev.remote.NetworkResult
import com.martdev.remote.RemoteDataSource
import com.martdev.remote.ResponseDataPayload
import com.martdev.remote.VIDEOS_PATH
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VideoRemoteDataSource(
    private val client: Client
) : RemoteDataSource<NetworkResult<ResponseDataPayload< List<VideoPostResponse>>>> {

    override fun load(): Flow<NetworkResult<ResponseDataPayload< List<VideoPostResponse>>>> {
        return flow {
            val result =
                client.getRequest<ResponseDataPayload< List<VideoPostResponse>>>(VIDEOS_PATH)
            emit(result)
        }
    }
}