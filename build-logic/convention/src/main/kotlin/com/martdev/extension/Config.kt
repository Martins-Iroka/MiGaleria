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
            fun getSecret(key: String): String {
                // Prioritize environment variable, then fall back to local.properties
                return System.getenv(key.uppercase()) ?: localProperties.getProperty(key) ?: ""
            }
            getByName("debug") {
                buildConfigField("String", "BASE_URL", "\"${getSecret("base_url")}\"")
                buildConfigField("String", "KEY_ALIAS", "\"${getSecret("key_alias")}\"")
            }
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
                val baseUrl = getSecret("base_url")
                val keyAlias = getSecret("key_alias")

                buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
                buildConfigField("String", "KEY_ALIAS", "\"$keyAlias\"")
            }
        }
    }

}