package com.ch4k4uw.workout.egym.navigation

data class BottomBarNavigationController(
    private val onNavigationStateChanged: (enable: Boolean) -> Unit
) {
    fun enableNavigation(enable: Boolean) {
        onNavigationStateChanged(enable)
    }
}