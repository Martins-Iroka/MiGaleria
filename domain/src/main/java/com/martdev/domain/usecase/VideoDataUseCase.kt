package com.martdev.domain.usecase

import com.martdev.domain.Repository
import com.martdev.domain.Video

class VideoDataUseCase(private val videoDataRepo: Repository<Video>) :
    UseCase<Video> {

    override fun invoke(query: String, networkConnected: Boolean) =
        videoDataRepo.getData(query, networkConnected)
}