package com.ch4k4uw.workout.egym.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ch4k4uw.workout.egym.login.LoginScreen
import com.ch4k4uw.workout.egym.login.LoginViewModel
import com.ch4k4uw.workout.egym.state.AppState

@ExperimentalUnitApi
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) { navBackStackEntry ->
            val factory = HiltViewModelFactory(LocalContext.current, navBackStackEntry)
            val viewModel: LoginViewModel = viewModel(
                key = LoginViewModel::class.java.simpleName, factory = factory
            )
            LoginScreen(
                uiState = viewModel.uiState.collectAsState(initial = AppState.Idle()),
                onIntent = viewModel::performIntent,
                onSuccessfulLoggedIn = { }
            )
        }
    }
}