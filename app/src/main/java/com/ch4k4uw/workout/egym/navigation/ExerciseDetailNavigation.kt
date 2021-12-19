package com.ch4k4uw.workout.egym.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ch4k4uw.workout.egym.exercise.detail.ExerciseDetailScreen
import com.ch4k4uw.workout.egym.exercise.detail.ExerciseDetailViewModel
import com.ch4k4uw.workout.egym.extensions.viewModel

fun NavGraphBuilder.exerciseDetailNavigation(
    route: String = Screen.Home.Exercise.Detail.route,
    navigationState: NavigationState
) {
    composable(
        route = route
    ) { navBackStackEntry ->
        val viewModel: ExerciseDetailViewModel =
            navBackStackEntry.viewModel()
        ExerciseDetailScreen(
            uiState = viewModel.uiState,
            onShowExerciseList = {
                navigationState
                    .backPressOwner
                    ?.onBackPressedDispatcher
                    ?.onBackPressed()
            },
            onNavigateBack = {
                navigationState
                    .backPressOwner
                    ?.onBackPressedDispatcher
                    ?.onBackPressed()
            },
            onIntent = viewModel::performIntent
        )
    }
}