package com.ch4k4uw.workout.egym.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ch4k4uw.workout.egym.exercise.list.ExerciseListScreen
import com.ch4k4uw.workout.egym.exercise.list.ExerciseListViewModel
import com.ch4k4uw.workout.egym.extensions.RestoreWindowBarsEffect
import com.ch4k4uw.workout.egym.extensions.viewModel

fun NavGraphBuilder.exerciseListNavigation(navigationState: NavigationState) {
    composable(
        route = Screen.Home.Exercise.List.route
    ) { navBackStackEntry ->
        val viewModel: ExerciseListViewModel = navBackStackEntry.viewModel()
        ExerciseListScreen(
            uiState = viewModel.uiState,
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
            },
            onExerciseClick = {
                navigationState.navController
                    .navigate(
                        route = Screen.Home.Exercise.Detail.route(it)
                    )
            }
        )
        RestoreWindowBarsEffect(navigationState = navigationState)
    }
}