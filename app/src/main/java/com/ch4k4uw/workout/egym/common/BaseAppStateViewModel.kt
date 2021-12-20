package com.ch4k4uw.workout.egym.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ch4k4uw.workout.egym.common.state.AppState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.io.Serializable

abstract class BaseAppStateViewModel<State : Serializable, Intent> : ViewModel() {
    private val mutableUiState = MutableSharedFlow<AppState<State>>(replay = 0)
    val uiState: Flow<AppState<State>> = mutableUiState
    var isActivated = false

    init {
        mutableUiState
            .subscriptionCount
            .map { it > 0 }
            .distinctUntilChanged()
            .onEach { activated ->
                isActivated = activated
            }
            .launchIn(viewModelScope)
    }

    protected suspend fun emitLoading(tag: State? = null) = emit(AppState.Loading(tag = tag))

    private suspend fun emit(value: AppState<State>) {
        while(!isActivated) yield()
        mutableUiState.emit(value)
    }

    protected suspend fun emitLoaded(tag: State? = null) = emit(AppState.Loaded(tag = tag))

    protected suspend fun emitError(cause: Throwable, tag: State? = null) =
        emit(AppState.Error(cause = cause, tag = tag))

    protected suspend fun emitSuccess(value: State) =
        emit(AppState.Success(value))

    open fun performIntent(intent: Intent) {
    }
}