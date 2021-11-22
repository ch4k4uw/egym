package com.ch4k4uw.workout.egym.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ch4k4uw.workout.egym.state.AppState

val State<AppState<*>>.isLoading: Boolean
    get() = value.isLoading

val State<AppState<*>>.isIdle: Boolean
    get() = value.isIdle

@Composable
fun <T> State<AppState<T>>.raiseEvent(): AppState<T> =
    if (currentComposer.changed(value)) {
        value
    } else {
        AppState.Idle()
    }


fun <T> State<AppState<T>>.asSuccess(): AppState.Success<T>? =
    value as? AppState.Success<T>