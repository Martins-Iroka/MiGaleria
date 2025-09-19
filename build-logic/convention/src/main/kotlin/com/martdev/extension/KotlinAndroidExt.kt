package com.martdev.extension

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion

internal fun ApplicationExtension.configureKotlinAndroid() {
    apply {
        compileSdk = 36

        defaultConfig {
            minSdk = 26
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
}