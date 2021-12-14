package com.ch4k4uw.workout.egym.extensions

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.ch4k4uw.workout.egym.common.state.AppState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.Serializable

@Suppress("UNCHECKED_CAST")
private fun <T : Serializable> saver(
    queue: MutableList<AppState<T>>,
    calculation: (MutableList<AppState<T>>, AppState<T>) -> State<AppState<T>>
): Saver<State<AppState<T>>, *> = Saver(
    save = { Bundle().apply { putSerializable("me", it.value) } },
    restore = {
        it.getSerializable("me")
            ?.run {
                calculation(queue, this as AppState<T>)
            }
    }
)

@Composable
fun <T : Serializable> rememberSaveableDerivedUiState(
    queue: MutableList<AppState<T>>,
): State<AppState<T>> {
    fun calculation(
        queue: MutableList<AppState<T>>, initialState: AppState<T>
    ): State<AppState<T>> {
        var lastState: AppState<T> = initialState
        return derivedStateOf {
            if (queue.isNotEmpty()) {
                queue[0].also { lastState = it }
            } else {
                lastState
            }
        }
    }

    return rememberSaveable(saver = saver(queue = queue, calculation = ::calculation)) {
        calculation(queue = queue, initialState = AppState.Idle())
    }
}

@Composable
fun <T : Serializable> Flow<AppState<T>>.collectAsBufferedState(): State<AppState<T>> {
    val coroutineScope = rememberCoroutineScope()
    val uiStateQueue = remember {
        mutableStateListOf<AppState<T>>()
    }
    val uiState = rememberSaveableDerivedUiState(queue = uiStateQueue)
    LaunchedEffect(key1 = this, key2 = LocalContext.current) {
        coroutineScope.launch {
            collect {
                uiStateQueue.add(it)
            }
        }
    }
    LaunchedEffect(key1 = uiStateQueue.size) {
        launch {
            if (uiStateQueue.isNotEmpty()) {
                uiStateQueue.removeAt(0)
            }
        }
    }
    return uiState
}