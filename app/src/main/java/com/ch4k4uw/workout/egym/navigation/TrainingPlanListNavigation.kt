package com.ch4k4uw.workout.egym.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavGraphBuilder.trainingPlanListNavigation(navigationState: NavigationState) {
    composable(route = Screen.Home.Plan.List.route) {
    }
}