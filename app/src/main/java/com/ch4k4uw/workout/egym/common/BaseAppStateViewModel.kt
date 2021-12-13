package com.ch4k4uw.workout.egym.common

import androidx.lifecycle.ViewModel
import com.ch4k4uw.workout.egym.common.state.AppState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

abstract class BaseAppStateViewModel<State, Intent> : ViewModel() {
    private val mutableUiState = MutableSharedFlow<AppState<State>>(replay = 1)
    val uiState: Flow<AppState<State>> = mutableUiState

    protected suspend fun emitLoading(tag: State? = null) = emit(AppState.Loading(tag = tag))

    private suspend fun emit(value: AppState<State>) =
        mutableUiState.emit(value)

    protected suspend fun emitLoaded(tag: State? = null) = emit(AppState.Loaded(tag = tag))

    protected suspend fun emitError(cause: Throwable, tag: State? = null) =
        emit(AppState.Error(cause = cause, tag = tag))

    protected suspend fun emitSuccess(value: State) =
        emit(AppState.Success(value))

    open fun performIntent(intent: Intent) {
    }
}