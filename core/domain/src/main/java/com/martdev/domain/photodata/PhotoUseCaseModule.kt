package com.martdev.domain.photodata

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val photoUseCaseModule = module {
    factoryOf(::PhotoDataUseCase)
}