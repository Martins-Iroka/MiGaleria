plugins {
    alias(libs.plugins.com.martdev.android.feature)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.martdev.video"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.compose.network)
    implementation(libs.compose.paging)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui.compose)
    implementation(libs.paging)
    implementation(libs.paging.common)
    androidTestImplementation(libs.paging.testing)
    testImplementation(libs.paging.testing)
    androidTestImplementation(libs.google.truth)
    testImplementation(libs.google.truth)
}