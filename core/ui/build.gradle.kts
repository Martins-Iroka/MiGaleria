plugins {
    alias(libs.plugins.com.martdev.android.library)
    alias(libs.plugins.com.martdev.android.compose.library)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.martdev.ui"
}

dependencies {
    api(libs.bundles.navigation3)
    debugApi(libs.bundles.composeToolingLib)
}
