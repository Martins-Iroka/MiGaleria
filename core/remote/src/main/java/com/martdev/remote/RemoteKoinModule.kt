package com.martdev.remote

import com.martdev.remote.login.userLoginRemoteModule
import com.martdev.remote.photo.photoRemoteModule
import com.martdev.remote.registration.userRegistrationRemoteModule
import com.martdev.remote.verification.userVerificationRemoteModule
import com.martdev.remote.video.videoRemoteModule
import org.koin.dsl.module

val remoteKoinModule = module {
    includes(
        photoRemoteModule,
        userLoginRemoteModule,
        userRegistrationRemoteModule,
        userVerificationRemoteModule,
        videoRemoteModule
    )
}