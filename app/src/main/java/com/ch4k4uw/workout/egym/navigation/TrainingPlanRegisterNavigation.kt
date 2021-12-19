package com.ch4k4uw.workout.egym.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ch4k4uw.workout.egym.common.state.AppState
import com.ch4k4uw.workout.egym.extensions.RestoreWindowBarsEffect
import com.ch4k4uw.workout.egym.extensions.viewModel
import com.ch4k4uw.workout.egym.training.plan.list.TrainingPlanListViewModel
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanListIntent
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanView
import com.ch4k4uw.workout.egym.training.plan.register.TrainingPlanRegisterScreen
import com.ch4k4uw.workout.egym.training.plan.register.interaction.TrainingPlanRegisterState
import kotlinx.coroutines.flow.flowOf

fun NavGraphBuilder.trainingPlanRegisterNavigation(navigationState: NavigationState) {
    composable(
        route = Screen.Home.Plan.Register.route,
        arguments = Screen.Home.Plan.Register.args.map {
            navArgument(name = it.first) {
                nullable = it.second
                defaultValue = it.third
            }
        }
    ) { _ ->
        val plaListViewModel: TrainingPlanListViewModel? =
            navigationState.navController.previousBackStackEntry.viewModel()
        TrainingPlanRegisterScreen(
            events = flowOf(
                AppState.Success(
                    content = TrainingPlanRegisterState.ShowPlan(plan = TrainingPlanView.Empty)
                )
            ),
            onDetailExercise = {
                navigationState.navController
                    .navigate(
                        route = Screen.Home.Plan.Detail.route(it)
                    )
            },
            onNavigateBack = {
                navigationState.navController.navigateUp()
                plaListViewModel?.performIntent(intent = TrainingPlanListIntent.FetchPlanList)
            },
        )
        RestoreWindowBarsEffect(navigationState = navigationState)
    }
}