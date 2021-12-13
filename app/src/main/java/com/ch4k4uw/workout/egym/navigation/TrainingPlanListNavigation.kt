package com.ch4k4uw.workout.egym.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ch4k4uw.workout.egym.extensions.RestoreWindowBarsEffect
import com.ch4k4uw.workout.egym.extensions.collectAsBufferedState
import com.ch4k4uw.workout.egym.extensions.viewModel
import com.ch4k4uw.workout.egym.training.plan.list.TrainingPlanListScreen
import com.ch4k4uw.workout.egym.training.plan.list.TrainingPlanListViewModel

fun NavGraphBuilder.trainingPlanListNavigation(navigationState: NavigationState) {
    composable(route = Screen.Home.Plan.List.route) { navBackStackEntry ->
        val viewModel: TrainingPlanListViewModel = navBackStackEntry.viewModel()
        TrainingPlanListScreen(
            uiState = viewModel.uiState.collectAsBufferedState(),
            onIntent = viewModel::performIntent,
            onLoggedOut = {
                navigationState.navController
                    .navigate(route = Screen.Login.route) {
                        popUpTo(
                            route = navBackStackEntry.destination.route
                                ?: ""
                        ) {
                            inclusive = true
                        }
                    }
            },
            onNavigateBack = {
                navigationState
                    .backPressOwner
                    ?.onBackPressedDispatcher
                    ?.onBackPressed()
            }
        )
        RestoreWindowBarsEffect(navigationState = navigationState)
    }
}