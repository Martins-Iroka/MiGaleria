plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotzilla)
}

android {
    namespace = "com.martdev.android.mygallery"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.martdev.android.mygallery"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    kotzilla {
        composeInstrumentation = true
    }

    packaging {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("io.ktor:ktor-client-android:1.2.5")

    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation(projects.core.domain)
    implementation(projects.core.remote)
    implementation(projects.core.local)
    implementation(projects.core.ui)
    implementation(libs.kotzilla.sdk.compose)
    implementation(libs.bundles.koinLibsWithCompose)
    // Jetpack Compose
    val composeBom = platform(libs.compose.bom.get())
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(libs.bundles.composeLibs)

    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation(libs.bundles.composeToolingLib)
}