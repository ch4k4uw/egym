package com.ch4k4uw.workout.egym.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.ch4k4uw.workout.egym.common.state.AppState
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.core.ui.LocalAppInsetsPaddingValues
import com.ch4k4uw.workout.egym.exercise.detail.ExerciseDetailScreen
import com.ch4k4uw.workout.egym.exercise.detail.ExerciseDetailViewModel
import com.ch4k4uw.workout.egym.exercise.list.ExerciseListScreen
import com.ch4k4uw.workout.egym.exercise.list.ExerciseListViewModel
import com.ch4k4uw.workout.egym.extensions.CollectEachState
import com.ch4k4uw.workout.egym.extensions.viewModel
import com.ch4k4uw.workout.egym.login.LoginScreen
import com.ch4k4uw.workout.egym.login.LoginViewModel

@Composable
fun Navigation() {
    val navigationState = rememberNavigationState()

    BottomBarNavigation(
        navController = navigationState.navController,
        show = navigationState.isShowBottomNavigator,
        screens = listOf(
            Screen.Home.Exercise,
            Screen.Home.Plan
        ),
    ) {
        NavHost(
            navController = navigationState.navController,
            startDestination = navigationState.navController.tryGraph()?.startDestinationRoute
                ?: Screen.Login.route
        ) {
            loginNavigation(navigationState = navigationState)
            homeNestedNavigation {
                exerciseNestedNavigation {
                    exerciseListNavigation(navigationState = navigationState)
                    exerciseDetailNavigation(navigationState = navigationState)
                }
                trainingPlanNestedNavigation {
                    trainingPlanListNavigation(navigationState = navigationState)
                }
            }
        }
    }
}

fun NavHostController.tryGraph(): NavGraph? =
    try {
        graph
    } catch (e: Throwable) {
        null
    }