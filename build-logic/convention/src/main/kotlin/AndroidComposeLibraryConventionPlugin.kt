
import com.android.build.gradle.LibraryExtension
import com.martdev.extension.androidTestImplementation
import com.martdev.extension.implementation
import com.martdev.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.apply {
                apply("com.android.library")
            }

            val extension = extensions.getByType<LibraryExtension>()
            extension.run {
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