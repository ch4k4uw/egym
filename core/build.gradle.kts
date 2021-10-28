import extensions.versionSharing
import extensions.implementation
import extensions.kapt

plugins {
    id(Plugins.Android.LibraryModule)
    id(Plugins.Kotlin.Parcelize)
    id(Plugins.Google.Services)
    id(Plugins.DaggerHilt)
    id(Plugins.Kotlin.Kapt)
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.Compose
    }
}

dependencies {
    configureBaseUiDependencies()
    configureCompose()
    versionSharing(Libraries.Firebase.BoM)
    implementation(Libraries.Firebase.Auth)
    implementation(Libraries.Glide)
    implementation(Libraries.Google.DaggerHilt.Android)
    kapt(Libraries.Google.DaggerHilt.Compiler)
}