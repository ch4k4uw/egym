import extensions.versionSharing

plugins {
    id(Plugins.Android.LibraryModule)
    id(Plugins.Kotlin.Parcelize)
    id(Plugins.Google.Services)
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
}