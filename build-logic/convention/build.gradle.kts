import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.martdev.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "com.martdev.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidComposeLibrary") {
            id = "com.martdev.android.compose.library"
            implementationClass = "AndroidComposeLibraryConventionPlugin"
        }

        register("androidFeature") {
            id = "com.martdev.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }

        register("androidLibrary") {
            id = "com.martdev.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
    }
}