import extensions.platformImplementation

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
    platformImplementation(Libraries.Firebase.BoM)
    implementation(Libraries.Firebase.Auth)
}