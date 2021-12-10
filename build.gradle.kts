buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dependencies.Gradle)
        classpath(Dependencies.Kotlin)
        classpath(Dependencies.GoogleServices)
        classpath(Dependencies.DaggerHilt)
    }
}

allprojects {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

subprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
        kotlinOptions {
            freeCompilerArgs = listOf(
                "-Xopt-in=androidx.compose.material.ExperimentalMaterialApi",
                "-Xopt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                "-Xopt-in=androidx.compose.ui.unit.ExperimentalUnitApi",
                "-Xopt-in=kotlinx.coroutines.FlowPreview",
            )
            jvmTarget = "1.8"
        }
    }
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}