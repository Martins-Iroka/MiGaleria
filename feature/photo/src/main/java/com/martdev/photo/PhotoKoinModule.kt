package com.martdev.photo

import com.martdev.data.photosource.photoDataModule
import com.martdev.domain.photodata.photoUseCaseModule
import com.martdev.ui.reusable.AppNavigator
import com.martdev.ui.reusable.NavigateTo
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val photoModule = module {
    includes(photoUseCaseModule, photoDataModule)
    viewModelOf(::PhotoViewModel)
    activityRetainedScope {
        navigation<NavigateTo.Photo> {
            val navigator = get<AppNavigator>()
            PhotoComposeScreen(
                goToDetail = { postID, imageUrl ->
                    navigator.goTo(NavigateTo.PhotoDetail(postID, imageUrl))
                }
            ) {
                navigator.goBack()
            }
        }

        navigation<NavigateTo.PhotoDetail> {
            val navigator = get<AppNavigator>()
            PhotoDetailCompose(
                it.postId,
                it.imageUrl
            ) {
                navigator.goBack()
            }
        }
    }
}