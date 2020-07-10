package com.martdev.android.data.usecase

import com.martdev.android.data.Repository
import com.martdev.android.domain.Result
import com.martdev.android.domain.photomodel.Photo

class PhotoDataUseCase(private val photoRepo: Repository<Photo>) :
    UseCase<Photo> {

    override suspend fun invoke(query: String, networkConnected: Boolean): Result<List<Photo>> {
        return photoRepo.getData(query, networkConnected)
    }
}