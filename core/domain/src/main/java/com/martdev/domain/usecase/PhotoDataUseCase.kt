package com.martdev.domain.usecase

import com.martdev.domain.Photo
import com.martdev.domain.Repository

class PhotoDataUseCase(private val photoRepo: Repository<Photo>) :
    UseCase<Photo> {

    override fun invoke(query: String) =
        photoRepo.getData(query)
}