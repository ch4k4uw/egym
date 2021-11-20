import extensions.versionSharing
import extensions.implementation
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
        debug {
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
            buildConfigField("String", "TABLE_EXERCISE", "\"exercise\"")
            buildConfigField("String", "TABLE_TRAINING_PLAN", "\"dev-training-plan\"")
            buildConfigField("String", "TABLE_TRAINING_EXECUTION", "\"dev-training-exec\"")
        }
        release {
            buildConfigField("String", "TABLE_EXERCISE", "\"exercise\"")
            buildConfigField("String", "TABLE_TRAINING_PLAN", "\"training-plan\"")
            buildConfigField("String", "TABLE_TRAINING_EXECUTION", "\"training-exec\"")
        }
    }
}

dependencies {
    configureBaseUiDependencies()
    configureCompose()
    versionSharing(Libraries.Firebase.BoM)
    implementation(Libraries.Firebase.Auth)
    implementation(Libraries.Firebase.Firestore)
    implementation(Libraries.Google.Gms.Auth)
    implementation(Libraries.Glide)

    //Accompanist
    implementation(Libraries.Accompanist.SysUIController)
    implementation(Libraries.Accompanist.Insets)

    //Hilt
    implementation(Libraries.Google.Hilt.Android)
    kapt(Libraries.Google.Hilt.Compiler)
}