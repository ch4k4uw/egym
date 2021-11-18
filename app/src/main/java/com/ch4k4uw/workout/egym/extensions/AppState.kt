package com.ch4k4uw.workout.egym.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.currentComposer
import com.ch4k4uw.workout.egym.state.AppState

val State<AppState<*>>.isLoading: Boolean
    @Composable
    get() = value is AppState.Loading<*>

@Composable
fun <T> State<AppState<T>>.HandleEvent(eventHandler: AppState<T>.() -> Unit) {
    if (currentComposer.changed(value)) {
        eventHandler(value)
    }
}