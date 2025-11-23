plugins {
    alias(libs.plugins.com.martdev.android.feature)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.martdev.verification"
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(projects.core.ui)
}