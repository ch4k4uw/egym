package com.ch4k4uw.workout.egym.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ch4k4uw.workout.egym.common.state.AppState
import com.ch4k4uw.workout.egym.exercise.detail.ExerciseDetailScreen
import com.ch4k4uw.workout.egym.exercise.detail.ExerciseDetailViewModel
import com.ch4k4uw.workout.egym.extensions.viewModel

fun NavGraphBuilder.exerciseDetailNavigation(navigationState: NavigationState) {
    composable(
        route = Screen.Home.Exercise.Detail.route
    ) { navBackStackEntry ->
        val viewModel: ExerciseDetailViewModel =
            navBackStackEntry.viewModel()
        ExerciseDetailScreen(
            uiState = viewModel
                .uiState
                .collectAsState(initial = AppState.Idle()),
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