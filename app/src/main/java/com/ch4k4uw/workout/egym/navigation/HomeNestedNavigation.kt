package com.ch4k4uw.workout.egym.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

fun NavGraphBuilder.homeNestedNavigation(navGraphBuilder: NavGraphBuilder.() -> Unit) {
    navigation(
        route = Screen.Home.route,
        startDestination = Screen.Home.Exercise.route,
        builder = navGraphBuilder
    )
}