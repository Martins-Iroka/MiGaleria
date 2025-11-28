package com.martdev.registration

import com.martdev.data.registration.userRegistrationDataModule
import com.martdev.domain.registration.userRegistrationUseCaseModule
import com.martdev.ui.reusable.NavigateTo
import com.martdev.ui.reusable.Navigator
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val userRegistrationModule = module {
    includes(userRegistrationUseCaseModule, userRegistrationDataModule)
    viewModelOf(::UserRegistrationViewModel)
    activityRetainedScope {
        navigation<NavigateTo.Registration> {
            val navigator = get<Navigator>()
            UserRegistrationScreen(
                goToLogin = {
                    navigator.goBack()
                },
                goToVerification = {
                    navigator.goTo(NavigateTo.Verification(it))
                }
            )
        }
    }
}