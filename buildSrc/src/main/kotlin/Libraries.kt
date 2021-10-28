object Libraries {
    object Compose {
        const val Runtime = "androidx.compose.runtime:runtime:${Versions.Compose.Compose}"
        const val RuntimeLiveData = "androidx.compose.runtime:runtime-livedata:${Versions.Compose.Compose}"
        const val Ui = "androidx.compose.ui:ui:${Versions.Compose.Compose}"
        const val Material = "androidx.compose.material:material:${Versions.Compose.Compose}"
        const val Icons = "androidx.compose.material:material-icons-core:${Versions.Compose.Icons}"
        const val IconsExtended = "androidx.compose.material:material-icons-extended:${Versions.Compose.Icons}"
        const val UiTooling = "androidx.compose.ui:ui-tooling:${Versions.Compose.Compose}"
        const val Foundation = "androidx.compose.foundation:foundation:${Versions.Compose.Compose}"
        const val Compiler = "androidx.compose.compiler:compiler:${Versions.Compose.Compose}"
        const val ConstraintLayout = "androidx.constraintlayout:constraintlayout-compose:${Versions.Compose.ConstraintLayout}"
        const val Activity = "androidx.activity:activity-ktx:${Versions.Compose.Activity}"
    }

    object Kotlin {
        const val StdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.Kotlin.Gradle}"
        const val Coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.Coroutines}"
        const val CoroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.Coroutines}"
        const val CoroutinesPlayServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.Kotlin.CoroutinesPlayServices}"
    }

    object LifeCycle {
        const val Runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LifeCycle}"
        const val LiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LifeCycle}"
        const val ViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LifeCycle}"
        const val Kapt = "androidx.lifecycle:lifecycle-compiler:${Versions.LifeCycle}"
        const val Compiler = "androidx.lifecycle:lifecycle-common-java8:${Versions.LifeCycle}"
    }

    const val JavaDesugaring = "com.android.tools:desugar_jdk_libs:${Versions.JavaDesugaring}"

    object AndroidX {
        object Ktx {
            const val Core = "androidx.core:core-ktx:${Versions.AndroidX.Ktx.Core}"
        }
        const val AppCompat = "androidx.appcompat:appcompat:${Versions.AndroidX.AppCompat}"
    }

    object Google {
        const val Material = "com.google.android.material:material:${Versions.Google.Material}"
    }

    object Firebase {
        const val BoM = "com.google.firebase:firebase-bom:${Versions.Firebase.BoM}"
        const val Auth = "com.google.firebase:firebase-auth-ktx"
        const val Firestore = "com.google.firebase:firebase-firestore-ktx"
        const val RemoteConfig = "com.google.firebase:firebase-config-ktx"
    }

    object DaggerHilt {

    }

    const val Glide = "com.github.bumptech.glide:glide:${Versions.Glide}"
}