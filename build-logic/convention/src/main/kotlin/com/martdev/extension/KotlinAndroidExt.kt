package com.martdev.extension

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

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

internal fun Project.configureKotlinJvm() {
    extensions.configure<KotlinAndroidProjectExtension> {
        jvmToolchain(17)
    }
}