plugins {
    alias(libs.plugins.com.martdev.android.library)
}

android {
    namespace = "com.martdev.common"
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
}