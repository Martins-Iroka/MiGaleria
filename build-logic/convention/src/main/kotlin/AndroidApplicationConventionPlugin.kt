
import com.android.build.api.dsl.ApplicationExtension
import com.martdev.extension.androidTestImplementation
import com.martdev.extension.com_android_application
import com.martdev.extension.configureBuild
import com.martdev.extension.configureKotlinJvm
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

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }

                configureBuild(this)
                configureKotlinJvm()
                buildFeatures {
                    compose = true
                    buildConfig = true
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