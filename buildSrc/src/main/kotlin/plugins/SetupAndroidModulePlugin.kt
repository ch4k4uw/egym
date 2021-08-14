package plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import configureAsAndroidLibrary
import configureBaseDependencies

class SetupAndroidModulePlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            plugins.apply(Plugins.Android.Library)
            plugins.apply("kotlin-${Plugins.Kotlin.Android}")
            configureAsAndroidLibrary()
            configureBaseDependencies()
        }
    }
}