package com.ch4k4uw.workout.egym.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.ch4k4uw.workout.egym.state.AppState

val State<AppState<*>>.isLoading: Boolean
    @Composable
    get() = value is AppState.Loading<*>