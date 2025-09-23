plugins {
    alias(libs.plugins.com.martdev.android.library)
}

android {
    namespace = "com.martdev.domain"

    kotlinOptions {
        jvmTarget = "17"
    }
}
