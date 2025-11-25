package com.martdev.remote

import com.martdev.remote.login.userLoginModule
import com.martdev.remote.photo.photoRemoteModule
import com.martdev.remote.registration.userRegistrationModule
import com.martdev.remote.verification.userVerificationModule
import com.martdev.remote.video.videoRemoteModule
import org.koin.dsl.module

val remoteKoinModule = module {
    includes(
        photoRemoteModule,
        userLoginModule,
        userRegistrationModule,
        userVerificationModule,
        videoRemoteModule
    )
}