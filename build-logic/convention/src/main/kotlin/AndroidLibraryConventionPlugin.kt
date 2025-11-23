
import com.android.build.gradle.LibraryExtension
import com.martdev.extension.androidTestImplementation
import com.martdev.extension.com_android_library
import com.martdev.extension.configureBuild
import com.martdev.extension.implementation
import com.martdev.extension.libs
import com.martdev.extension.org_jetbrains_kotlin_android
import com.martdev.extension.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply(com_android_library)
                apply(org_jetbrains_kotlin_android)
            }

            extensions.configure<LibraryExtension> {

                compileSdk = 36

                packaging {
                    resources.excludes.add("META-INF/DEPENDENCIES")
                    resources.excludes.add("META-INF/LICENSE")
                    resources.excludes.add("META-INF/LICENSE.txt")
                    resources.excludes.add("META-INF/license.txt")
                    resources.excludes.add("META-INF/NOTICE")
                    resources.excludes.add("META-INF/NOTICE.txt")
                    resources.excludes.add("META-INF/notice.txt")
                    resources.excludes.add("META-INF/ASL2.0")
                    resources.excludes.add("META-INF/INDEX.LIST")
                    resources.excludes.add("META-INF/io.netty.versions.properties")
                    resources.excludes.add("META-INF/NOTICE.md")
                    resources.excludes.add("META-INF/LICENSE.md")
                    resources.excludes.add("META-INF/LICENSE-notice.md")
                    resources.excludes.add("META-INF/*.kotlin_module")
                }

                defaultConfig.run {
                    targetSdk = 36
                    minSdk = 26
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                configureBuild(this)
                buildFeatures {
                    buildConfig = true
                }

                dependencies {
                    androidTestImplementation(libs.findBundle("androidTestLibs").get())
                    implementation(libs.findLibrary("androidx-core-ktx").get())
                    implementation(libs.findLibrary("coroutines-core").get())
                    implementation(libs.findLibrary("coroutines-test").get())
                    implementation(libs.findBundle("koinLibs").get())
                    implementation(libs.findLibrary("timber").get())
                    testImplementation(libs.findBundle("testLibs").get())
                    testImplementation(kotlin("test"))
                }
            }
        }
    }
}