package com.ch4k4uw.workout.egym.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.ch4k4uw.workout.egym.common.state.AppState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun <T> Flow<AppState<T>>.CollectEachState(content: @Composable (State<AppState<T>>) -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    val uiStateQueue = remember {
        mutableStateListOf<AppState<T>>()
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
        coroutineScope.launch {
            collect {
                uiStateQueue.add(it)
            }
        }
    }
    LaunchedEffect(key1 = uiStateQueue.size) {
        if (uiStateQueue.isNotEmpty()) {
            uiStateQueue.removeAt(0)
        }
    }
}

