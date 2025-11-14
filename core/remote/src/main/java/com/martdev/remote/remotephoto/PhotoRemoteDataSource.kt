package com.martdev.remote.remotephoto

import com.martdev.remote.Client
import com.martdev.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal const val CURATED_PHOTO = "v1/photos"

class PhotoRemoteDataSource(
    private val client: Client
) : RemoteDataSource<PhotoPostResponsePayload> {

    override fun load(): Flow<PhotoPostResponsePayload> {
        return flow {
            val result = client.performGetRequest<PhotoPostResponsePayload>(CURATED_PHOTO)
            emit(result)
        }
    }
}