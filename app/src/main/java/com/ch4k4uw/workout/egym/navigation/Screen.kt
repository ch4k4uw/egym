package com.ch4k4uw.workout.egym.navigation

sealed class Screen(val route: String) {
    object Login : Screen(route = "login")
    object Home : Screen(route = "home")
}
