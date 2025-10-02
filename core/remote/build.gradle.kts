plugins {
    alias(libs.plugins.com.martdev.android.library)
    id("kotlinx-serialization")
}

android {
    namespace = "com.martdev.remote"
}

dependencies {
    implementation(libs.bundles.ktorLibs)
}