package com.ch4k4uw.workout.egym.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.ui.graphics.vector.ImageVector
import com.ch4k4uw.workout.egym.R

sealed class Screen(
    val route: String,
    @StringRes val label: Int = 0,
    val icon: ImageVector? = null,
    val parent: Screen? = null
) {
    object Login : Screen(route = "login")
    object Home : Screen(route = "home") {
        object Exercise : Screen(
            route = "home/exercise",
            label = R.string.navigation_exercise_list_label,
            icon = Icons.Filled.FitnessCenter,
            parent = Home
        ) {
            object List : Screen(
                route = "home/exercise/list"
            )
            object Detail : Screen(
                route = "home/exercise/detail/{exerciseId}"
            ) {
                fun route(exerciseId: String) =
                    route.replace("{exerciseId}", exerciseId)
            }
        }

        object Plan : Screen(
            route = "home/plan",
            label = R.string.navigation_exercise_plans_label,
            icon = Icons.Filled.Assignment,
            parent = Home
        ) {
            object List : Screen(
                route = "home/plan/list"
            )
        }
    }
}
