package com.martdev.android.data.usecase

import com.martdev.android.data.Repository
import com.martdev.android.data.SourceResult
import com.martdev.android.domain.videomodel.Video
import kotlinx.coroutines.CoroutineScope

class VideoDataUseCase(private val videoDataRepo: Repository<Video>) :
    UseCase<Video> {
    override fun invoke(
        query: String?,
        scope: CoroutineScope,
        networkConnected: Boolean
    ): SourceResult<Video> = videoDataRepo.getData(query, scope, networkConnected)
}