
import com.android.build.gradle.LibraryExtension
import com.martdev.extension.implementation
import com.martdev.extension.libs
import com.martdev.extension.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.martdev.android.library")
                apply("com.martdev.android.compose.library")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            extensions.configure<LibraryExtension> {

                dependencies {
                    implementation(project(":core:data"))
                    implementation(project(":core:ui"))
                    implementation(libs.findBundle("koinLibsWithCompose").get())
//                    implementation(libs.findLibrary("kotzilla-sdk-compose").get())
                    implementation(libs.findBundle("navigation3").get())
                    testImplementation(project(":core:test-shared"))
                }
            }
        }
    }
}