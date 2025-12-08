plugins {
    alias(libs.plugins.com.martdev.android.feature)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.martdev.photo"
}

dependencies {
    implementation(libs.compose.coil)
    implementation(libs.compose.coil.network)
    implementation(libs.compose.paging)
    implementation(libs.paging)
    implementation(libs.paging.common)
    androidTestImplementation(libs.paging.testing)
    testImplementation(libs.paging.testing)
    androidTestImplementation(libs.google.truth)
    testImplementation(libs.google.truth)
}