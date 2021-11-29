package com.ch4k4uw.workout.egym.navigation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun BottomBarNavigation(
    navController: NavHostController,
    show: State<Boolean>,
    screens: List<Screen>,
    content: @Composable (PaddingValues) -> Unit
) {
    val bottomBarProperties = object {
        private var offset by remember { mutableStateOf(0.dp) }
        private var animate by remember { mutableStateOf(false) }
        val floatAnimation by animateDpAsState(
            targetValue = offset,
            animationSpec = if (animate) tween(durationMillis = 300) else snap()
        )

        fun setTo(offset: Dp) {
            animate = false
            this.offset = offset
        }

        fun animateTo(offset: Dp) {
            animate = true
            this.offset = offset
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .absoluteOffset(y = bottomBarProperties.floatAnimation)
                    .navigationBarsPadding()
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currDestination = navBackStackEntry?.destination
                screens.forEach { screen ->
                    if (screen.icon != null) {
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = null
                                )
                            },
                            label = { Text(text = stringResource(id = screen.label)) },
                            selected = currDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(id = navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }

            }
        }
    ) { paddingValues ->
        if (show.value) {
            bottomBarProperties
                .animateTo(0.dp)
        } else {
            bottomBarProperties
                .setTo(paddingValues.calculateBottomPadding())
        }
        content(PaddingValues(bottom = bottomBarProperties.floatAnimation))
    }
}