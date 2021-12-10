package com.ch4k4uw.workout.egym.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation

fun NavGraphBuilder.trainingPlanNestedNavigation(navGraphBuilder: NavGraphBuilder.() -> Unit) {
    navigation(
        route = Screen.Home.Plan.route,
        startDestination = Screen.Home.Plan.List.route,
        builder = navGraphBuilder
    )
}