import extensions.versionSharing
import extensions.implementation
import extensions.kapt
import java.util.Properties

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

    buildTypes {
        getByName("debug") {
            val propertyFile = "../google.properties"
            val properties = file(propertyFile).exists().let { fileStatus ->
                if (fileStatus) {
                    val stream = file(propertyFile).inputStream()
                    Properties().also {
                        it.load(stream)
                        stream.close()
                    }
                } else {
                    null
                }
            }
            if (properties != null) {
                buildConfigField(
                    "String",
                    "DEFAULT_WEB_CLIENT_ID",
                    "\"${properties.getProperty("requestIdToken")}\""
                )
            }
        }
    }
}

dependencies {
    configureBaseUiDependencies()
    configureCompose()
    versionSharing(Libraries.Firebase.BoM)
    implementation(Libraries.Firebase.Auth)
    implementation(Libraries.Google.Gms.Auth)
    implementation(Libraries.Glide)
    implementation(Libraries.Google.DaggerHilt.Android)
    kapt(Libraries.Google.DaggerHilt.Compiler)
}