package com.martdev.extension

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    applicationExtension: CommonExtension<*, *, *, *, *>
) {
    applicationExtension.run {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("compose-bom").get()
            add(implementation, platform(bom))
            add(androidTestImplementation, platform(bom))
        }
    }
}