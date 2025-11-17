package com.martdev.remote.remotephoto

import com.martdev.remote.Client
import com.martdev.remote.NetworkResult
import com.martdev.remote.PHOTOS_PATH
import com.martdev.remote.RemoteDataSource
import com.martdev.remote.ResponseDataPayload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PhotoRemoteDataSource(
    private val client: Client
) : RemoteDataSource<NetworkResult<ResponseDataPayload<List<PhotoSrcAPI>>>> {

    override fun load(): Flow<NetworkResult<ResponseDataPayload<List<PhotoSrcAPI>>>> {
        return flow {
            val result = client.getRequest<ResponseDataPayload<List<PhotoSrcAPI>>>(PHOTOS_PATH)
            emit(result)
        }
    }
}