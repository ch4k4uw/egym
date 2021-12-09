package com.ch4k4uw.workout.egym.navigation

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.ch4k4uw.workout.egym.common.state.AppState
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.exercise.detail.ExerciseDetailScreen
import com.ch4k4uw.workout.egym.exercise.detail.ExerciseDetailViewModel
import com.ch4k4uw.workout.egym.exercise.list.ExerciseListScreen
import com.ch4k4uw.workout.egym.exercise.list.ExerciseListViewModel
import com.ch4k4uw.workout.egym.extensions.CollectEachState
import com.ch4k4uw.workout.egym.extensions.viewModel
import com.ch4k4uw.workout.egym.login.LoginScreen
import com.ch4k4uw.workout.egym.login.LoginViewModel
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.FlowPreview

@ExperimentalMaterialApi
@FlowPreview
@ExperimentalComposeUiApi
@ExperimentalUnitApi
@Composable
fun Navigation() {
    val isDark = isSystemInDarkTheme()
    val systemUiController = rememberSystemUiController()
    val navController = rememberNavController()
    val showBottomNavigator = remember { mutableStateOf(false) }
    val backPressOwner = LocalOnBackPressedDispatcherOwner.current

    BottomBarNavigation(
        navController = navController,
        show = showBottomNavigator,
        screens = listOf(
            Screen.Home.Exercise,
            Screen.Home.Plan
        ),
    ) { paddingValues, _ ->
        NavHost(
            navController = navController,
            startDestination = navController.tryGraph()?.startDestinationRoute
                ?: Screen.Login.route
        ) {
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
                            navController.graph.setStartDestination(
                                startDestRoute = Screen.Home.route
                            )
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
                startDestination = Screen.Home.Exercise.route
            ) {
                navigation(
                    route = Screen.Home.Exercise.route,
                    startDestination = Screen.Home.Exercise.List.route
                ) {
                    composable(
                        route = Screen.Home.Exercise.List.route
                    ) { navBackStackEntry ->
                        val viewModel: ExerciseListViewModel = navBackStackEntry.viewModel()
                        viewModel.uiState.CollectEachState { uiState ->
                            Box(
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .padding(
                                        PaddingValues(bottom = paddingValues.value.calculateBottomPadding())
                                    )
                            ) {
                                ExerciseListScreen(
                                    uiState = uiState,
                                    onIntent = viewModel::performIntent,
                                    onLoggedOut = {
                                        navController
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
                                        backPressOwner?.onBackPressedDispatcher?.onBackPressed()
                                    },
                                    onExerciseClick = {
                                        navController
                                            .navigate(
                                                route = Screen.Home.Exercise.Detail.route(it)
                                            ) {
                                                restoreState = true
                                            }
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
                    composable(
                        route = Screen.Home.Exercise.Detail.route
                    ) { navBackStackEntry ->
                        val viewModel: ExerciseDetailViewModel =
                            navBackStackEntry.viewModel()
                        Box(
                            modifier = Modifier
                                .statusBarsPadding()
                                .padding(
                                    PaddingValues(bottom = paddingValues.value.calculateBottomPadding())
                                )
                        ) {
                            ExerciseDetailScreen(
                                uiState = viewModel
                                    .uiState
                                    .collectAsState(initial = AppState.Idle()),
                                onShowExerciseList = {
                                    backPressOwner?.onBackPressedDispatcher?.onBackPressed()
                                },
                                onNavigateBack = {
                                    backPressOwner?.onBackPressedDispatcher?.onBackPressed()
                                }
                            )
                        }
                    }
                }
                navigation(
                    route = Screen.Home.Plan.route,
                    startDestination = Screen.Home.Plan.List.route
                ) {
                    composable(route = Screen.Home.Plan.List.route) {
                    }
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