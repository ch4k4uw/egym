package com.ch4k4uw.workout.egym.extensions

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.core.ui.LocalAppInsetsPaddingValues
import com.ch4k4uw.workout.egym.navigation.NavigationState
import java.io.Serializable

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

inline fun <reified T: Serializable> createSerializableStateListSaver(): Saver<SnapshotStateList<T>, *> =
    Saver(
        save = { Bundle().apply { putSerializable("data", it.toTypedArray()) } },
        restore = { savedState ->
            val data = savedState.getSerializable("data") as Array<*>
            data
                .map { it as T }
                .let {
                    SnapshotStateList<T>().apply {
                        addAll(it)
                    }
                }
        }
    )