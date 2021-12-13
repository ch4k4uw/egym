package com.ch4k4uw.workout.egym.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.SideEffect
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.core.ui.LocalAppInsetsPaddingValues
import com.ch4k4uw.workout.egym.navigation.NavigationState

@NonRestartableComposable
@Composable
fun RestoreWindowBarsEffect(navigationState: NavigationState) {
    val barColor = AppTheme.colors.material.primaryVariant
    val insetsPaddingValues = LocalAppInsetsPaddingValues.current
    SideEffect {
        navigationState.systemUiController.setSystemBarsColor(
            color = barColor
        )
        navigationState.showBottomNavigator()
        insetsPaddingValues.enableInsets()
    }
}