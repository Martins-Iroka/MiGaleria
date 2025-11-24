package com.martdev.remote

import com.martdev.remote.client.clientModule
import com.martdev.remote.datastore.tokenModuleModule
import com.martdev.remote.login.userLoginModule
import com.martdev.remote.photo.photoRemoteModule
import com.martdev.remote.registration.userRegistrationModule
import com.martdev.remote.verification.userVerificationModule
import com.martdev.remote.video.videoRemoteModule
import org.koin.dsl.module

val remoteKoinModule = module {
    includes(
        clientModule,
        photoRemoteModule,
        tokenModuleModule,
        userLoginModule,
        userRegistrationModule,
        userVerificationModule,
        videoRemoteModule
    )
}