
package com.ch4k4uw.workout.egym.extensions

import com.ch4k4uw.workout.egym.state.AppState

val AppState<*>.isLoading: Boolean
    get() = this is AppState.Loading

val AppState<*>.isIdle: Boolean
    get() = this is AppState.Idle

val AppState<*>.isSuccess: Boolean
    get() = this is AppState.Success

fun <T> AppState<T>.handleSuccess(handler: AppState.Success<T>.() -> Unit) {
    asSuccess()?.also {
        handler(it)
    }
}

fun <T> AppState<T>.asSuccess(): AppState.Success<T>? =
    (this as? AppState.Success<T>)

fun <T> AppState<T>.handleError(handler: AppState.Error<T>.() -> Unit) {
    asError()?.also {
        handler(it)
    }
}

fun <T> AppState<T>.asError(): AppState.Error<T>? =
    (this as? AppState.Error<T>)

fun <T> AppState<T>.asLoading(): AppState.Loading<T>? =
    (this as? AppState.Loading<T>)