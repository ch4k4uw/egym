package com.ch4k4uw.workout.egym.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ch4k4uw.workout.egym.extensions.RestoreWindowBarsEffect
import com.ch4k4uw.workout.egym.extensions.parseStringArgAsParcelable
import com.ch4k4uw.workout.egym.extensions.viewModel
import com.ch4k4uw.workout.egym.training.plan.list.TrainingPlanListViewModel
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanListIntent
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanView
import com.ch4k4uw.workout.egym.training.plan.register.TrainingPlanRegisterScreen
import com.ch4k4uw.workout.egym.training.plan.register.TrainingPlanRegisterViewModel

fun NavGraphBuilder.trainingPlanRegisterNavigation(navigationState: NavigationState) {
    composable(
        route = Screen.Home.Plan.Register.route,
        arguments = Screen.Home.Plan.Register.args.map {
            navArgument(name = it.first) {
                nullable = it.second
                defaultValue = it.third
            }
        }
    ) { navBackStackEntry ->
        navBackStackEntry.parseStringArgAsParcelable<TrainingPlanView>(
            key = Screen.Home.Plan.Register.ArgsNames.PlanMetadata,
            decoder = navigationState.routeEncode.decodeFromRoute
        )
        val planListViewModel: TrainingPlanListViewModel? =
            navigationState.navController.previousBackStackEntry.viewModel()
        val planRegisterViewModel: TrainingPlanRegisterViewModel =
            navBackStackEntry.viewModel()
        TrainingPlanRegisterScreen(
            events = planRegisterViewModel.uiState,
            onDetailExercise = {
                navigationState.navController
                    .navigate(
                        route = Screen.Home.Plan.Detail.route(it)
                    )
            },
            onNavigateBack = {
                navigationState.navController.navigateUp()
                planListViewModel?.performIntent(intent = TrainingPlanListIntent.FetchPlanList)
            },
            onIntent = planRegisterViewModel::performIntent
        )
        RestoreWindowBarsEffect(navigationState = navigationState)
    }
}