
import com.android.build.api.dsl.ApplicationExtension
import com.martdev.extension.androidTestImplementation
import com.martdev.extension.com_android_application
import com.martdev.extension.implementation
import com.martdev.extension.libs
import com.martdev.extension.org_jetbrains_kotlin_android
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(com_android_application)
                apply(org_jetbrains_kotlin_android)
            }

            extensions.configure<ApplicationExtension> {
                compileSdk = 36

                defaultConfig {
                    applicationId = "com.martdev.android.mygallery"
                    minSdk = 26
                    targetSdk = 36
                    versionCode = 1
                    versionName = "1.0"

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                packaging {
                    resources.excludes.add("META-INF/*.kotlin_module")
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }

                buildFeatures {
                    compose = true
                }

                dependencies {
                    val bom = libs.findLibrary("compose-bom").get()
                    add(implementation, platform(bom))
                    add(androidTestImplementation, platform(bom))
                }
            }
        }
    }

}