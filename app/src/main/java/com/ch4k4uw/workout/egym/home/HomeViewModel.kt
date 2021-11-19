package com.ch4k4uw.workout.egym.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ch4k4uw.workout.egym.home.domain.interactor.HomeInteractor
import com.ch4k4uw.workout.egym.home.interaction.HomeIntent
import com.ch4k4uw.workout.egym.home.interaction.HomeState
import com.ch4k4uw.workout.egym.login.interaction.UserView
import com.ch4k4uw.workout.egym.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val interactor: HomeInteractor,
    savedState: SavedStateHandle
) : ViewModel() {
    private val mutableUiState = MutableSharedFlow<AppState<HomeState>>(replay = 1)
    val uiState: Flow<AppState<HomeState>> = mutableUiState

    init {
        savedState.get<UserView>("user")?.also {
            viewModelScope.launch {
                emitSuccess(HomeState.DisplayUserData(user = it))
            }
        }
    }

    private suspend fun <T : HomeState> emitSuccess(value: T) =
        emit(AppState.Success(value))

    private suspend fun <T : AppState<HomeState>> emit(value: T) =
        mutableUiState.emit(value)

    fun performIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.PerformLogout -> performLogout()
        }
    }

    private fun performLogout() {
        viewModelScope.launch {
            emit(AppState.Loading())
            interactor.performLogout()
                .catch {
                    emit(AppState.Loaded())
                    emitError(it)
                    it.printStackTrace()
                }
                .collect {
                    emit(AppState.Loaded())
                    emitSuccess(HomeState.ShowLoginScreen)
                }
        }
    }

    private suspend fun emitError(cause: Throwable) =
        emit(AppState.Error(cause))
}