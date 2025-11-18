plugins {
    alias(libs.plugins.com.martdev.android.library)
    id("kotlinx-serialization")
}

android {
    namespace = "com.martdev.remote"
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(libs.bundles.ktorLibs)
    implementation(libs.androidx.datastore.preferences)
    implementation(projects.core.common)
}