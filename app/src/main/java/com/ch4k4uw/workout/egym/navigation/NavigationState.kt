package com.ch4k4uw.workout.egym.navigation

import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ch4k4uw.workout.egym.injection.RouteEncodeEntryPoint
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.EntryPoints

@Stable
class NavigationState(
    val isDark: Boolean,
    val routeEncode: RouteEncodeEntryPoint,
    val systemUiController: SystemUiController,
    val navController: NavHostController,
    isShowBottomNavigator: Boolean,
    val backPressOwner: OnBackPressedDispatcherOwner?
) {
    private val mutableIsShowBottomNavigator = mutableStateOf(isShowBottomNavigator)
    val isShowBottomNavigator: State<Boolean> = mutableIsShowBottomNavigator

    fun showBottomNavigator() {
        mutableIsShowBottomNavigator.value = true
    }

    fun hideBottomNavigator() {
        mutableIsShowBottomNavigator.value = false
    }
}

@Composable
fun rememberNavigationState(
    isDark: Boolean = isSystemInDarkTheme(),
    systemUiController: SystemUiController = rememberSystemUiController(),
    navController: NavHostController = rememberNavController(),
    isShowBottomNavigator: Boolean = false,
    backPressOwner: OnBackPressedDispatcherOwner? = LocalOnBackPressedDispatcherOwner.current
): NavigationState {
    val context = LocalContext.current
    val routeEncode = remember(context) {
        EntryPoints.get(context.applicationContext, RouteEncodeEntryPoint::class.java)
    }
    return remember(isDark, routeEncode) {
        NavigationState(
            isDark = isDark,
            routeEncode = routeEncode,
            systemUiController = systemUiController,
            navController = navController,
            isShowBottomNavigator = isShowBottomNavigator,
            backPressOwner = backPressOwner
        )
    }
}