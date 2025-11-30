package com.martdev.verification

import com.martdev.data.verification.userVerificationDataModule
import com.martdev.domain.verification.userVerificationUseCaseModule
import com.martdev.ui.reusable.NavigateTo
import com.martdev.ui.reusable.Navigator
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val userVerificationModule = module {
    includes(
        userVerificationUseCaseModule,
        userVerificationDataModule
    )
    viewModelOf(::UserVerificationViewModel)
    activityRetainedScope {
        navigation<NavigateTo.Verification> {
            val navigator = get<Navigator>()
            UserVerificationScreen(
                it.email
            ) {
                navigator.popUpTo<NavigateTo.Login>()
            }
        }
    }
}