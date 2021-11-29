package com.ch4k4uw.workout.egym.navigation

import android.annotation.SuppressLint
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.exercise.ExerciseListScreen
import com.ch4k4uw.workout.egym.exercise.ExerciseListViewModel
import com.ch4k4uw.workout.egym.extensions.CollectEachState
import com.ch4k4uw.workout.egym.extensions.viewModel
import com.ch4k4uw.workout.egym.login.LoginScreen
import com.ch4k4uw.workout.egym.login.LoginViewModel
import com.ch4k4uw.workout.egym.state.AppState
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@SuppressLint("UnrememberedMutableState")
@ExperimentalUnitApi
@Composable
fun Navigation() {
    val isDark = isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()
    val navController = rememberNavController()
    val showBottomNavigator = remember { mutableStateOf(false) }
    BottomBarNavigation(
        navController = navController,
        show = showBottomNavigator,
        screens = listOf(
            Screen.Home.ExerciseList,
            Screen.Home.ExercisePlans
        )
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = Screen.Login.route) {
            composable(route = Screen.Login.route) { navBackStackEntry ->
                val viewModel: LoginViewModel = navBackStackEntry.viewModel()
                Box(
                    modifier = Modifier
                        .navigationBarsPadding()
                ) {
                    LoginScreen(
                        uiState = viewModel.uiState.collectAsState(initial = AppState.Idle()),
                        onIntent = viewModel::performIntent,
                        onSuccessfulLoggedIn = {
                            navController.navigate(
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
                }
                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = !isDark
                    )
                    showBottomNavigator.value = false
                }
            }
            navigation(
                route = Screen.Home.route,
                startDestination = Screen.Home.ExerciseList.route
            ) {
                composable(
                    route = Screen.Home.ExerciseList.route
                ) { navBackStackEntry ->
                    val viewModel: ExerciseListViewModel = navBackStackEntry.viewModel()
                    val backPressOwner = LocalOnBackPressedDispatcherOwner.current
                    viewModel.uiState.CollectEachState { uiState ->
                        Box(
                            modifier = Modifier
                                .padding(
                                    PaddingValues(bottom = paddingValues.calculateBottomPadding())
                                )
                        ) {
                            ExerciseListScreen(
                                uiState = uiState,
                                onIntent = viewModel::performIntent,
                                onLoggedOut = {
                                    navController
                                        .navigate(route = Screen.Login.route) {
                                            popUpTo(
                                                route = navBackStackEntry.destination.route ?: ""
                                            ) {
                                                inclusive = true
                                            }
                                        }
                                },
                                onNavigateBack = {
                                    backPressOwner?.onBackPressedDispatcher?.onBackPressed()
                                }
                            )
                        }
                    }
                    val barColor = AppTheme.colors.material.primaryVariant
                    SideEffect {
                        systemUiController.setSystemBarsColor(
                            color = barColor
                        )
                        showBottomNavigator.value = true
                    }
                }
            }
        }
    }
}