plugins {
    alias(libs.plugins.com.martdev.android.library)
}

android {
    namespace = "com.martdev.data"
}

dependencies {
    api(projects.core.domain)
    api(projects.core.local)
    api(projects.core.remote)
    api(projects.core.common)
}