package com.ch4k4uw.workout.egym.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.ch4k4uw.workout.egym.state.AppState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

@Composable
fun <T> Flow<AppState<T>>.CollectEachState(content: @Composable (State<AppState<T>>) -> Unit) {
    var uiStateQueue by remember {
        mutableStateOf<MutableList<AppState<T>>>(
            mutableListOf()
        )
    }
    val uiState = remember {
        var lastState: AppState<T> = AppState.Idle()
        derivedStateOf {
            if (uiStateQueue.isNotEmpty()) {
                uiStateQueue[0].also { lastState = it }
            } else {
                lastState
            }
        }
    }
    content(uiState)
    LaunchedEffect(key1 = this, key2 = LocalContext.current) {
        collect {
            uiStateQueue = uiStateQueue.toMutableList().apply {
                add(it)
            }
        }
    }
    LaunchedEffect(key1 = uiStateQueue.size) {
        if (uiStateQueue.isNotEmpty()) {
            uiStateQueue = uiStateQueue.toMutableList().apply {
                removeAt(0)
            }
        }
        cancel()
    }
}