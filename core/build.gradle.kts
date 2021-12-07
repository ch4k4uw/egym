import extensions.versionSharing
import extensions.implementation
import extensions.api
import extensions.kapt
import java.util.Properties

plugins {
    id(Plugins.Android.LibraryModule)
    id(Plugins.Kotlin.Parcelize)
    id(Plugins.Hilt)
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
        fun readProperties(propertyFile: String) = file(propertyFile).exists().let { fileStatus ->
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

        all {
            val googleProperties = readProperties(propertyFile = "../google.properties")
            val apiKeysProperties = readProperties(propertyFile = "../api-keys.properties")

            if (googleProperties != null) {
                buildConfigField(
                    "String",
                    "DEFAULT_WEB_CLIENT_ID",
                    "\"${googleProperties.getProperty("requestIdToken")}\""
                )
            }

            if (apiKeysProperties != null) {
                buildConfigField(
                    "String",
                    "API_AUTH",
                    "\"${apiKeysProperties.getProperty("key1")}\""
                )
            }
        }
        
        debug {
            buildConfigField("String", "TABLE_EXERCISE", "\"exercise\"")
            buildConfigField("String", "TABLE_TRAINING_PLAN", "\"dev-training-plan\"")
            buildConfigField("String", "TABLE_TRAINING_EXECUTION", "\"dev-training-exec\"")
            buildConfigField("String", "HELPER_API_URL", "\"https://egymfunc.ddns.net:8080\"")
        }
        release {
            buildConfigField("String", "TABLE_EXERCISE", "\"exercise\"")
            buildConfigField("String", "TABLE_TRAINING_PLAN", "\"training-plan\"")
            buildConfigField("String", "TABLE_TRAINING_EXECUTION", "\"training-exec\"")
            buildConfigField("String", "HELPER_API_URL", "\"https://egymfunc.ddns.net:8080\"")
        }
    }
}

dependencies {
    configureBaseUiDependencies()
    configureComposeDependencies()
    configureNetworkingDependencies()
    implementation(Libraries.LoggingInterceptor)
    versionSharing(Libraries.Firebase.BoM)
    implementation(Libraries.Firebase.Auth)
    implementation(Libraries.Firebase.Firestore)
    implementation(Libraries.Google.Gms.Auth)
    implementation(Libraries.Glide)
    api(Libraries.Timber)

    //Accompanist
    implementation(Libraries.Accompanist.SysUIController)
    implementation(Libraries.Accompanist.Insets)

    //Hilt
    implementation(Libraries.Google.Hilt.Android)
    kapt(Libraries.Google.Hilt.Compiler)
}