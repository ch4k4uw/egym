package extensions

import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

fun DependencyHandlerScope.implementation(dependecy: String) =
    add("implementation", dependecy)

fun DependencyHandlerScope.testImplementation(dependecy: String) =
    add("testImplementation", dependecy)

fun DependencyHandlerScope.internalModule(dependecy: String) =
    add("implementation", project(dependecy))

fun DependencyHandlerScope.kapt(dependecy: String) =
    add("kapt", dependecy)

fun DependencyHandlerScope.internalApi(dependecy: String) =
    add("api", project(dependecy))

fun DependencyHandlerScope.versionSharing(dependecy: String) =
    add("implementation", platform(dependecy))
