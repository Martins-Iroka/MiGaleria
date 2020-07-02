package com.martdev.android.data.usecase

import com.martdev.android.data.Repository
import com.martdev.android.data.SourceResult
import com.martdev.android.domain.photomodel.Photo
import kotlinx.coroutines.CoroutineScope

class PhotoDataUseCase(private val photoRepo: Repository<Photo>) :
    UseCase<Photo> {
    override fun invoke(
        query: String?,
        scope: CoroutineScope,
        networkConnected: Boolean
    ): SourceResult<Photo> = photoRepo.getData(query, scope, networkConnected)
}