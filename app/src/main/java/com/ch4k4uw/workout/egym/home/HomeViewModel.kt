package com.ch4k4uw.workout.egym.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ch4k4uw.workout.egym.home.interaction.HomeState
import com.ch4k4uw.workout.egym.login.interaction.UserView
import com.ch4k4uw.workout.egym.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
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
}