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
    val parent: Screen? = null,
    val args: List<Triple<String, Boolean, Any?>> = listOf(),
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
            object Register : Screen(
                route = "home/plan/register?planMetadata={planMetadata}",
                args = listOf<Triple<String, Boolean, Any?>>(
                    Triple("planMetadata", true, null as String?)
                )
            ) {
                object ArgsNames {
                    const val PlanMetadata = "planMetadata"
                }
                fun route(planMetadata: String?) =
                    planMetadata
                        ?.let { route.replace("{planMetadata}", it) }
                        ?: route.substring(0 until route.lastIndexOf('?'))
            }
            object Detail : Screen(
                route = "home/plan/exercise/detail/{exerciseId}"
            ) {
                fun route(exerciseId: String) =
                    route.replace("{exerciseId}", exerciseId)
            }
        }
    }
}
