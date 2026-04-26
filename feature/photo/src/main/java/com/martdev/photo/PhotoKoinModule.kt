package com.martdev.photo

import com.martdev.data.photosource.photoDataModule
import com.martdev.domain.photodata.photoUseCaseModule
import com.martdev.photo.photodetail.PhotoDetailCompose
import com.martdev.photo.photodetail.PhotoDetailViewModel
import com.martdev.ui.reusable.AppNavigator
import com.martdev.ui.reusable.NavigateTo
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val photoModule = module {
    includes(photoUseCaseModule, photoDataModule)
    viewModelOf(::PhotoViewModel)
    viewModelOf(::PhotoDetailViewModel)
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
            val viewModel2: PhotoDetailViewModel = koinViewModel {
                parametersOf(it.postId.toString())
            }
            PhotoDetailCompose(
                it.postId,
                it.imageUrl,
                viewModel2
            ) {
                navigator.goBack()
            }
        }
    }
}