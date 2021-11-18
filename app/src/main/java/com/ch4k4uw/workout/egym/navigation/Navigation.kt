package com.ch4k4uw.workout.egym.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.extensions.parseStringArgAsParcelable
import com.ch4k4uw.workout.egym.extensions.viewModel
import com.ch4k4uw.workout.egym.home.HomeScreen
import com.ch4k4uw.workout.egym.home.HomeViewModel
import com.ch4k4uw.workout.egym.injection.RouteEncodeEntryPoint
import com.ch4k4uw.workout.egym.login.LoginScreen
import com.ch4k4uw.workout.egym.login.LoginViewModel
import com.ch4k4uw.workout.egym.login.interaction.UserView
import com.ch4k4uw.workout.egym.state.AppState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.EntryPoints

@ExperimentalUnitApi
@Composable
fun Navigation() {
    val isDark = isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()
    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext
    val routeEncode = remember {
        EntryPoints.get(context, RouteEncodeEntryPoint::class.java)
    }

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) { navBackStackEntry ->
            val viewModel: LoginViewModel = navBackStackEntry.viewModel()
            LoginScreen(
                uiState = viewModel.uiState.collectAsState(initial = AppState.Idle()),
                onIntent = viewModel::performIntent,
                onSuccessfulLoggedIn = {
                    navController.navigate(
                        route = "${Screen.Home.route}/${routeEncode.encodeToRoute.encode(it)}"
                    ) {
                        popUpTo(
                            route = Screen.Login.route
                        ) {
                            inclusive = true
                        }
                    }
                }
            )

            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = Color.Transparent,
                    darkIcons = !isDark
                )
            }
        }
        composable(
            route = "${Screen.Home.route}/{user}",
            arguments = listOf(
                navArgument("user") {
                    type = NavType.StringType
                }
            )
        ) { navBackStackEntry ->
            remember {
                navBackStackEntry.parseStringArgAsParcelable<UserView>(
                    key = "user",
                    decoder = routeEncode.decodeFromRoute
                )
            }
            val viewModel: HomeViewModel = navBackStackEntry.viewModel()
            HomeScreen(
                uiState = viewModel.uiState.collectAsState(initial = AppState.Idle())
            )

            val barColor = AppTheme.colors.material.primaryVariant
            SideEffect {
                systemUiController.setSystemBarsColor(
                    color = barColor,
                    darkIcons = !isDark
                )
            }
        }
    }
}