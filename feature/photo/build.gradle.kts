plugins {
    alias(libs.plugins.com.martdev.android.feature)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.martdev.photo"
}

dependencies {
    implementation(libs.compose.paging)
    implementation(libs.paging)
    implementation(libs.paging.common)
    testImplementation(libs.paging.testing)
    testImplementation(libs.google.truth)
}