package com.ch4k4uw.workout.egym.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ch4k4uw.workout.egym.core.ui.LocalAppInsetsPaddingValues
import java.lang.Integer.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun BottomBarNavigation(
    navController: NavHostController,
    show: State<Boolean>,
    screens: List<Screen>,
    content: @Composable () -> Unit
) {

    val floatAnimation = animateFloatAsState(
        targetValue = if (show.value) 1f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    Layout(
        modifier = Modifier
            .padding(LocalAppInsetsPaddingValues.current.paddingValues.value),
        content = {
            Box(
                modifier = Modifier
                    .layoutId(0)
                    .fillMaxSize()
            ) {
                content()
            }
            BottomNavigation(
                modifier = Modifier
                    .layoutId(1)
                    .fillMaxWidth()
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
                            selected = currDestination?.hierarchy?.any {
                                it.route == screen.route
                            } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(
                                        route = screen.parent?.route
                                            ?: navController
                                                .graph
                                                .findStartDestination()
                                                .route
                                                .orEmpty()
                                    ) {
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
    ) { measurable, constraints ->
        val bottomBarPlaceable = measurable.find { it.layoutId == 1 }!!.measure(constraints)
        val bottomBarRequiredHeight =
            (bottomBarPlaceable.height * floatAnimation.value).roundToInt()
        val contentPlaceable = measurable
            .find { it.layoutId == 0 }!!
            .measure(
                constraints = constraints.copy(
                    maxHeight = constraints.maxHeight - bottomBarRequiredHeight,
                    minHeight = min(
                        a = constraints.minHeight,
                        b = constraints.maxHeight - bottomBarRequiredHeight
                    )
                )
            )

        val wMax = max(contentPlaceable.width, bottomBarPlaceable.width)
        val hMax = contentPlaceable.height + (bottomBarRequiredHeight * floatAnimation.value)
            .roundToInt()

        layout(wMax, hMax) {
            contentPlaceable.place(x = 0, y = 0)
            bottomBarPlaceable.place(
                x = 0,
                y = contentPlaceable
                    .height + bottomBarPlaceable
                    .height * (1f - floatAnimation.value)
                    .roundToInt()
            )
        }
    }
}