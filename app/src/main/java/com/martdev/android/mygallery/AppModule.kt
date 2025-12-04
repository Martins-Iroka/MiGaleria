package com.martdev.android.mygallery

import com.martdev.login.userLoginModule
import com.martdev.photo.photoModule
import com.martdev.registration.userRegistrationModule
import com.martdev.ui.reusable.AppNavigator
import com.martdev.ui.reusable.NavigateTo
import com.martdev.verification.userVerificationModule
import org.koin.androidx.scope.dsl.activityRetainedScope
import org.koin.dsl.module

val appModule = module {
    includes(
        userLoginModule,
        userRegistrationModule,
        userVerificationModule,
        photoModule
    )

    activityRetainedScope {
        scoped {
            AppNavigator(startDestination = NavigateTo.Login)
        }
    }
}