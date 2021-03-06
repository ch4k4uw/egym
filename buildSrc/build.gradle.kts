import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:7.0.4")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
}

gradlePlugin {
    plugins {
        register("android-library-module") {
            id = "android-library-module"
            implementationClass = "plugins.SetupAndroidModulePlugin"
        }
    }
}