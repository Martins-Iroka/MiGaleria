package com.martdev.domain.videodata

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val videoUseCaseModule = module {
    factoryOf(::VideoDataUseCase)
}