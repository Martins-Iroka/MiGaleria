import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.martdev.android.library")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
        }
    }
}