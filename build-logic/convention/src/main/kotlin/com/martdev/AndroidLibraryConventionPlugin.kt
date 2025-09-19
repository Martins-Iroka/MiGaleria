package com.martdev

import com.android.build.gradle.LibraryExtension
import com.martdev.extension.com_android_library
import com.martdev.extension.configureAndroidCompose
import com.martdev.extension.org_jetbrains_kotlin_android
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(com_android_library)
                apply(org_jetbrains_kotlin_android)
            }

            extensions.configure<LibraryExtension> {

                compileSdk = 36

                packaging {
                    resources.excludes.add("META-INF/*.kotlin_module")
                }

                defaultConfig.run {
                    targetSdk = 36
                    minSdk = 26
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                configureAndroidCompose(this)
            }
        }
    }
}