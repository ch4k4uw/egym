package com.ch4k4uw.workout.egym.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

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
                    trainingPlanRegisterNavigation(navigationState = navigationState)
                    exerciseDetailNavigation(
                        route = Screen.Home.Plan.Detail.route,
                        navigationState = navigationState
                    )
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