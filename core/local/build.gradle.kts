plugins {
    alias(libs.plugins.com.martdev.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.martdev.local"
    packaging {
        resources.excludes.add("META-INF/*.kotlin_module")
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/LICENSE-notice.md")
    }
}

dependencies {
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)
}