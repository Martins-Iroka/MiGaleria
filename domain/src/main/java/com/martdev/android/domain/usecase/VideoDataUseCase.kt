package com.martdev.android.domain.usecase

import com.martdev.android.domain.Repository
import com.martdev.android.domain.Result
import com.martdev.android.domain.videomodel.Video

class VideoDataUseCase(private val videoDataRepo: Repository<Video>) :
    UseCase<Video> {

    override suspend fun invoke(query: String, networkConnected: Boolean): Result<List<Video>> {
        return videoDataRepo.getData(query, networkConnected)
    }
}