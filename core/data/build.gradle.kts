plugins {
    alias(libs.plugins.com.martdev.android.library)
}

android {
    namespace = "com.martdev.data"
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.local)
    implementation(projects.core.remote)
}