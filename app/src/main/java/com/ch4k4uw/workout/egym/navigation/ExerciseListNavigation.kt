package com.ch4k4uw.workout.egym.navigation

import androidx.compose.runtime.SideEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.core.ui.LocalAppInsetsPaddingValues
import com.ch4k4uw.workout.egym.exercise.list.ExerciseListScreen
import com.ch4k4uw.workout.egym.exercise.list.ExerciseListViewModel
import com.ch4k4uw.workout.egym.extensions.CollectEachState
import com.ch4k4uw.workout.egym.extensions.viewModel

fun NavGraphBuilder.exerciseListNavigation(navigationState: NavigationState) {
    composable(
        route = Screen.Home.Exercise.List.route
    ) { navBackStackEntry ->
        val viewModel: ExerciseListViewModel = navBackStackEntry.viewModel()
        viewModel.uiState.CollectEachState { uiState ->
            ExerciseListScreen(
                uiState = uiState,
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
                        ) {
                            restoreState = true
                        }
                }
            )
        }
        val barColor = AppTheme.colors.material.primaryVariant
        val insetsPaddingValues = LocalAppInsetsPaddingValues.current
        SideEffect {
            navigationState.systemUiController.setSystemBarsColor(
                color = barColor
            )
            navigationState.showBottomNavigator()
            insetsPaddingValues.enableInsets()
        }
    }
}