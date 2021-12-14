package com.ch4k4uw.workout.egym.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.currentComposer
import com.ch4k4uw.workout.egym.common.state.AppState
import java.io.Serializable

val State<AppState<*>>.isLoading: Boolean
    get() = value.isLoading

val State<AppState<*>>.isIdle: Boolean
    get() = value.isIdle

@Composable
fun <T : Serializable> State<AppState<T>>.raiseEvent(): AppState<T> =
    if (currentComposer.changed(value)) {
        value
    } else {
        AppState.Idle()
    }


fun <T : Serializable> State<AppState<T>>.asSuccess(): AppState.Success<T>? =
    value as? AppState.Success<T>