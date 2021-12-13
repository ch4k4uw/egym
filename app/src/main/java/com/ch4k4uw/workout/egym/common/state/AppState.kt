package com.ch4k4uw.workout.egym.common.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.remember

sealed class AppState<out T> {
    class Idle<out T> : AppState<T>()
    data class Loading<out T>(val tag: T? = null) : AppState<T>()
    data class Loaded<out T>(val tag: T? = null) : AppState<T>()
    data class Success<out T>(val content: T): AppState<T>()
    data class Error<out T>(val cause: Throwable, val tag: T? = null): AppState<T>()
}

@NonRestartableComposable
@Composable
fun <T> AppStateEffect(
    state: AppState<T>,
    block: AppState<T>.() -> Unit
) {
    remember(key1 = state) {
        object : RememberObserver {
            private var called = false
            override fun onAbandoned() {
            }
            override fun onForgotten() {
                called = true
            }
            override fun onRemembered() {
                if (!called) {
                    called = true
                    block(state)
                }
            }
        }
    }
}

@NonRestartableComposable
@Composable
fun <T> AppSuccessStateEffect(
    state: AppState<T>,
    block: AppState.Success<T>.() -> Unit
) {
    AppStateEffect(state = state) {
        if (this is AppState.Success<T>) {
            block()
        }
    }
}

@NonRestartableComposable
@Composable
fun <T> AppErrorStateEffect(
    state: AppState<T>,
    block: AppState.Error<T>.() -> Unit
) {
    AppStateEffect(state = state) {
        if (this is AppState.Error<T>) {
            block()
        }
    }
}