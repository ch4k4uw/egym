import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import extensions.implementation

fun Project.configureBaseDependencies() = dependencies {
    implementation(Libraries.Kotlin.StdLib)
    implementation(Libraries.Kotlin.Coroutines)
    implementation(Libraries.Kotlin.CoroutinesAndroid)

    add("coreLibraryDesugaring", Libraries.JavaDesugaring)
}

fun Project.configureBaseUiDependencies() = dependencies {
    implementation(Libraries.LifeCycle.Runtime)
    implementation(Libraries.LifeCycle.LiveData)
    implementation(Libraries.LifeCycle.ViewModel)
    implementation(Libraries.LifeCycle.Compiler)
}

fun Project.configureCompose() = dependencies {
    implementation(Libraries.Compose.Runtime)
    implementation(Libraries.Compose.RuntimeLiveData)
    implementation(Libraries.Compose.Ui)
    implementation(Libraries.Compose.Material)
    implementation(Libraries.Compose.UiTooling)
    implementation(Libraries.Compose.Foundation)
    implementation(Libraries.Compose.Compiler)
    implementation(Libraries.Compose.Activity)
    implementation(Libraries.Compose.ConstraintLayout)
}