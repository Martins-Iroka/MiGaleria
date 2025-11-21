plugins {
    alias(libs.plugins.com.martdev.android.library)
    alias(libs.plugins.com.martdev.android.compose.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.martdev.verification"
}

dependencies {
    implementation(projects.core.ui)
}