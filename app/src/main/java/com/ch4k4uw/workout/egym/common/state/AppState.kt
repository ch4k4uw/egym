package com.ch4k4uw.workout.egym.common.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.remember
import java.io.Serializable
import java.util.*

sealed class AppState<out T : Serializable>(
    val uuid: String = UUID.randomUUID().toString()
) : Serializable {
    class Idle<out T : Serializable> : AppState<T>()
    data class Loading<out T : Serializable>(val tag: T? = null) : AppState<T>()
    data class Loaded<out T : Serializable>(val tag: T? = null) : AppState<T>()
    data class Success<out T : Serializable>(val content: T) : AppState<T>()
    data class Error<out T : Serializable>(val cause: Throwable, val tag: T? = null) : AppState<T>()
}

@NonRestartableComposable
@Composable
fun <T : Serializable> AppStateEffect(
    state: AppState<T>,
    block: AppState<T>.() -> Unit
) {
    remember(key1 = state.uuid) {
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
fun <T : Serializable> AppSuccessStateEffect(
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
fun <T : Serializable> AppErrorStateEffect(
    state: AppState<T>,
    block: AppState.Error<T>.() -> Unit
) {
    AppStateEffect(state = state) {
        if (this is AppState.Error<T>) {
            block()
        }
    }
}