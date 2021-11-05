package com.ch4k4uw.workout.egym.state

sealed class AppState<out T> {
    class Idle<out T> : AppState<T>()
    class Loading<out T> : AppState<T>()
    class Loaded<out T> : AppState<T>()
    class LoadCanceled<out T> : AppState<T>()
    data class Success<out T>(val content: T): AppState<T>()
    data class Error<out T>(val cause: Throwable): AppState<T>()
}