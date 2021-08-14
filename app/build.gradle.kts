import configs.AndroidConfigs
import extensions.internalModule

plugins {
    id(Plugins.Android.Application)
    kotlin(Plugins.Kotlin.Android)
    id(Plugins.Kotlin.Parcelize)
    kotlin(Plugins.Kotlin.Kapt)
}

android {
    compileSdk = AndroidConfigs.Sdk.Compile

    defaultConfig {
        applicationId = "com.ch4k4uw.workout.egym"
        minSdk = AndroidConfigs.Sdk.Min
        targetSdk = AndroidConfigs.Sdk.Target
        versionCode = Versions.App.Code
        versionName = Versions.App.Name

        testInstrumentationRunner = AndroidConfigs.InstrumentationTestRunner
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.Compose
    }

    packagingOptions {
        resources.excludes += "META-INF/DEPENDENCIES.txt"
        resources.excludes += "META-INF/LICENSE.txt"
        resources.excludes += "META-INF/NOTICE.txt"
        resources.excludes += "META-INF/NOTICE"
        resources.excludes += "META-INF/LICENSE"
        resources.excludes += "META-INF/DEPENDENCIES"
        resources.excludes += "META-INF/notice.txt"
        resources.excludes += "META-INF/license.txt"
        resources.excludes += "META-INF/dependencies.txt"
        resources.excludes += "META-INF/LGPL2.1"
    }

}

configurations {
    api.configure {
        exclude(group = "androidx.annotation", module = "annotation")
    }
}

dependencies {
    configureBaseDependencies()
    configureBaseUiDependencies()
    configureCompose()

    internalModule(InternalModules.components)

    implementation(Libraries.AndroidX.Ktx.Core)
    implementation(Libraries.AndroidX.AppCompat)
    implementation(Libraries.Google.Material)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}