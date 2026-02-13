package com.martdev.video

import com.martdev.data.videosource.videoDataModule
import com.martdev.domain.videodata.videoUseCaseModule
import com.martdev.ui.reusable.AppNavigator
import com.martdev.ui.reusable.NavigateTo
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val videoModule = module {
    includes(videoUseCaseModule, videoDataModule)
    viewModelOf(::VideoViewModel)
    activityRetainedScope {
        navigation<NavigateTo.Video> {
            val navigator = get<AppNavigator>()
            VideoComposeScreen (
                { navigator.goBack() },
                { id, link ->
                    navigator.goTo(NavigateTo.VideoPlayer(id, link))}
            )
        }

        navigation<NavigateTo.VideoPlayer> {
            val navigator = get<AppNavigator>()
            VideoPlayerCompose(it.postId, it.videoUrl) {
                navigator.goBack()
            }
        }
    }
}