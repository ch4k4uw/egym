package com.ch4k4uw.workout.egym.exercise

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.exercise.domain.interactor.ExerciseListInteractor
import com.ch4k4uw.workout.egym.exercise.extensions.toView
import com.ch4k4uw.workout.egym.exercise.interaction.ExerciseListIntent
import com.ch4k4uw.workout.egym.exercise.interaction.ExerciseListState
import com.ch4k4uw.workout.egym.login.extensions.toView
import com.ch4k4uw.workout.egym.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    private val exerciseListInteractor: ExerciseListInteractor
) : ViewModel() {
    private val mutableUiState = MutableSharedFlow<AppState<ExerciseListState>>(replay = 1)
    val uiState: Flow<AppState<ExerciseListState>> = mutableUiState

    init {
        viewModelScope.launch {
            emit(AppState.Loading())
            exerciseListInteractor
                .findLoggedUser()
                .catch {
                    emit(AppState.Loaded())
                    emitError(cause = it)
                    it.printStackTrace()
                }
                .collect {
                    emit(AppState.Loaded())
                    if (it != User.Empty) {
                        emitSuccess(
                            value = ExerciseListState.DisplayUserData(user = it.toView())
                        )
                        loadExercisesHeads()
                    }
                }
        }
    }

    private suspend fun <T : AppState<ExerciseListState>> emit(value: T) =
        mutableUiState.emit(value)

    private suspend fun emitError(cause: Throwable) =
        emit(AppState.Error(cause))

    private suspend fun <T : ExerciseListState> emitSuccess(value: T) =
        emit(AppState.Success(value))

    private suspend fun loadExercisesHeads() {
        try {
            emit(AppState.Loading(tag = ExerciseListState.ExerciseListTag))
            exerciseListInteractor
                .findExercisesHeads()
                .single()
                .also { pager ->
                    pager
                        .next()
                        .catch {
                            emit(AppState.Loaded(tag = ExerciseListState.ExerciseListTag))
                            emit(AppState.Error(cause = it))
                            it.printStackTrace()
                        }
                        .collect { nextPager ->
                            emit(AppState.Loaded(tag = ExerciseListState.ExerciseListTag))
                            emitSuccess(
                                value = ExerciseListState.ShowExerciseList(
                                    exercises = nextPager.items.map { it.toView() }
                                )
                            )
                        }
                }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun performIntent(intent: ExerciseListIntent) {
        when (intent) {
            is ExerciseListIntent.PerformLogout -> performLogout()
        }
    }

    private fun performLogout() {
        viewModelScope.launch {
            emit(AppState.Loading())
            exerciseListInteractor.performLogout()
                .catch {
                    emit(AppState.Loaded())
                    emitError(it)
                    it.printStackTrace()
                }
                .collect {
                    emit(AppState.Loaded())
                    emitSuccess(ExerciseListState.ShowLoginScreen)
                }
        }
    }
}