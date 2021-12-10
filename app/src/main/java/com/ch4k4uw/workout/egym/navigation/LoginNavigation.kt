package com.ch4k4uw.workout.egym.navigation

import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ch4k4uw.workout.egym.common.state.AppState
import com.ch4k4uw.workout.egym.core.ui.LocalAppInsetsPaddingValues
import com.ch4k4uw.workout.egym.extensions.viewModel
import com.ch4k4uw.workout.egym.login.LoginScreen
import com.ch4k4uw.workout.egym.login.LoginViewModel

fun NavGraphBuilder.loginNavigation(navigationState: NavigationState) {
    composable(route = Screen.Login.route) { navBackStackEntry ->
        val viewModel: LoginViewModel = navBackStackEntry.viewModel()
        LoginScreen(
            uiState = viewModel.uiState.collectAsState(initial = AppState.Idle()),
            onIntent = viewModel::performIntent,
            onSuccessfulLoggedIn = {
                navigationState.navController.graph.setStartDestination(
                    startDestRoute = Screen.Home.route
                )
                navigationState.navController.navigate(
                    route = Screen.Home.route
                ) {
                    popUpTo(
                        route = Screen.Login.route
                    ) {
                        inclusive = true
                    }
                }
            }
        )
        val insetsPaddingValues = LocalAppInsetsPaddingValues.current
        SideEffect {
            navigationState.systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = !navigationState.isDark
            )
            navigationState.hideBottomNavigator()
            insetsPaddingValues.enableInsets(
                statusBar = false,
                navBar = false
            )
        }
    }
}