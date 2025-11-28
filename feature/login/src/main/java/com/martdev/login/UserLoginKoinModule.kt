package com.martdev.login

import com.martdev.data.login.userLoginDataModule
import com.martdev.domain.login.userLoginUseCaseModule
import com.martdev.ui.reusable.NavigateTo
import com.martdev.ui.reusable.Navigator
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val userLoginModule = module {
    includes(userLoginUseCaseModule, userLoginDataModule)
    viewModelOf(::UserLoginViewModel)
    activityRetainedScope {
        navigation<NavigateTo.Login> {
            val navigator = get<Navigator>()
            UserLoginScreen(
                goBack = {
                    navigator.goBack()
                },
                goToSignUp = {
                    navigator.goTo(NavigateTo.Registration)
                }
            )
        }
    }
}