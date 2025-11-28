package com.martdev.android.mygallery

import com.martdev.login.userLoginModule
import com.martdev.registration.userRegistrationModule
import com.martdev.ui.reusable.NavigateTo
import com.martdev.ui.reusable.Navigator
import com.martdev.verification.userVerificationModule
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.dsl.module

val appModule = module {
    includes(
        userLoginModule,
        userRegistrationModule,
        userVerificationModule
    )

    activityRetainedScope {
        scoped {
            Navigator(startDestination = NavigateTo.Login)
        }
    }
}