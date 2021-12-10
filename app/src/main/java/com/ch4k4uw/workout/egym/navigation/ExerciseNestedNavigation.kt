package com.ch4k4uw.workout.egym.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

fun NavGraphBuilder.exerciseNestedNavigation(navGraphBuilder: NavGraphBuilder.() -> Unit) {
    navigation(
        route = Screen.Home.Exercise.route,
        startDestination = Screen.Home.Exercise.List.route,
        builder = navGraphBuilder
    )
}