package com.martdev.extension

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project

fun Project.configureBuild(
    common: CommonExtension<*, *, *, *, *, *>
) {
    val localProperties = java.util.Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }

    common.run {
        buildTypes {
            getByName("debug") {
                buildConfigField("String", "PEXELS_API_KEY", "\"${localProperties.getProperty("authorizationKey")}\"")
            }
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                buildConfigField("String", "PEXELS_API_KEY", "\"${localProperties.getProperty("authorizationKey")}\"")
            }
        }
    }

}