package com.ch4k4uw.workout.egym.extensions

import com.ch4k4uw.workout.egym.common.state.AppState
import java.io.Serializable

val AppState<*>.isLoading: Boolean
    get() = this is AppState.Loading

val AppState<*>.isIdle: Boolean
    get() = this is AppState.Idle

val AppState<*>.isSuccess: Boolean
    get() = this is AppState.Success

fun <T : Serializable> AppState<T>.handleSuccess(handler: AppState.Success<T>.() -> Unit) {
    asSuccess()?.also {
        handler(it)
    }
}

fun <T : Serializable> AppState<T>.asSuccess(): AppState.Success<T>? =
    (this as? AppState.Success<T>)

fun <T : Serializable> AppState<T>.handleError(handler: AppState.Error<T>.() -> Unit) {
    asError()?.also {
        handler(it)
    }
}

fun <T : Serializable> AppState<T>.asError(): AppState.Error<T>? =
    (this as? AppState.Error<T>)

fun <T : Serializable> AppState<T>.asLoading(): AppState.Loading<T>? =
    (this as? AppState.Loading<T>)