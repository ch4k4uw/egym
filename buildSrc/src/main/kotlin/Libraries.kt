object Libraries {
    object Compose {
        const val Runtime = "androidx.compose.runtime:runtime:${Versions.Compose.Compose}"
        const val RuntimeLiveData = "androidx.compose.runtime:runtime-livedata:${Versions.Compose.Compose}"
        const val Ui = "androidx.compose.ui:ui:${Versions.Compose.Compose}"
        const val Material = "androidx.compose.material:material:${Versions.Compose.Compose}"
        const val Icons = "androidx.compose.material:material-icons-core:${Versions.Compose.Icons}"
        const val IconsExtended = "androidx.compose.material:material-icons-extended:${Versions.Compose.IconsExtended}"
        const val UiTooling = "androidx.compose.ui:ui-tooling:${Versions.Compose.Compose}"
        const val Foundation = "androidx.compose.foundation:foundation:${Versions.Compose.Compose}"
        const val Compiler = "androidx.compose.compiler:compiler:${Versions.Compose.Compose}"
        const val ConstraintLayout = "androidx.constraintlayout:constraintlayout-compose:${Versions.Compose.ConstraintLayout}"
        const val ActivityKtx = "androidx.activity:activity-ktx:${Versions.Compose.ActivityKtx}"
        const val Activity = "androidx.activity:activity-compose:${Versions.Compose.Activity}"
        const val Navigation = "androidx.navigation:navigation-compose:${Versions.Compose.Navigation}"
        const val HiltNavigation = "androidx.hilt:hilt-navigation:${Versions.Compose.Hilt.Navigation}"
        const val Annotation = "androidx.annotation:annotation:${Versions.Compose.Annotation}"
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
        const val Palette = "androidx.palette:palette:${Versions.AndroidX.Palette}"
    }
    object Google {
        const val Material = "com.google.android.material:material:${Versions.Google.Material}"
        object Gms {
            const val Auth = "com.google.android.gms:play-services-auth:${Versions.Google.Gms.Auth}"
        }
        object Hilt {
            const val Android = "com.google.dagger:hilt-android:${Versions.Hilt}"
            const val Compiler = "com.google.dagger:hilt-android-compiler:${Versions.Hilt}"
        }
    }
    object Firebase {
        const val BoM = "com.google.firebase:firebase-bom:${Versions.Firebase.BoM}"
        const val Auth = "com.google.firebase:firebase-auth-ktx"
        const val Firestore = "com.google.firebase:firebase-firestore-ktx"
        const val RemoteConfig = "com.google.firebase:firebase-config-ktx"
    }
    const val Glide = "com.github.bumptech.glide:glide:${Versions.Glide}"
    object Accompanist {
        const val SysUIController = "com.google.accompanist:accompanist-systemuicontroller:${Versions.Accompanist}"
        const val Insets = "com.google.accompanist:accompanist-insets:${Versions.Accompanist}"
        const val InsetsUi = "com.google.accompanist:accompanist-insets-ui:${Versions.Accompanist}"
        const val FlowLayout = "com.google.accompanist:accompanist-flowlayout:${Versions.Accompanist}"
        const val HorizontalPager = "com.google.accompanist:accompanist-pager:${Versions.Accompanist}"
    }
    object Retrofit {
        const val Retrofit = "com.squareup.retrofit2:retrofit:${Versions.Retrofit}"
        const val GSonConverter = "com.squareup.retrofit2:converter-gson:${Versions.Retrofit}"
    }
    const val LoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.LoggingInterceptor}"
    const val Timber = "com.jakewharton.timber:timber:${Versions.Timber}"
}