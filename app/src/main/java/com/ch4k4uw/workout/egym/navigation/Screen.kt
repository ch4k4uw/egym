package com.ch4k4uw.workout.egym.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.ui.graphics.vector.ImageVector
import com.ch4k4uw.workout.egym.R

sealed class Screen(val route: String, @StringRes val label: Int = 0, val icon: ImageVector? = null) {
    object Login : Screen(route = "login")
    object Home : Screen(route = "home") {
        object ExerciseList : Screen(
                route = "exercise/list",
                label = R.string.navigation_exercise_list_label,
                icon = Icons.Filled.FitnessCenter
            )

        object ExercisePlans : Screen(
            route = "exercise/plan/list",
            label = R.string.navigation_exercise_plans_label,
            icon = Icons.Filled.Assignment
        )
    }
}
