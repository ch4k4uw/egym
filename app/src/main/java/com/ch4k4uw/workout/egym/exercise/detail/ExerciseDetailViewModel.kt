package com.ch4k4uw.workout.egym.exercise.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ch4k4uw.workout.egym.core.exercise.domain.repository.ExerciseRepository
import com.ch4k4uw.workout.egym.exercise.detail.interaction.ExerciseDetailState
import com.ch4k4uw.workout.egym.exercise.extensions.toView
import com.ch4k4uw.workout.egym.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseDetailViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    private val mutableUiState = MutableSharedFlow<AppState<ExerciseDetailState>>(replay = 1)
    val uiState: Flow<AppState<ExerciseDetailState>> = mutableUiState

    init {
        viewModelScope.launch {
            emit(AppState.Loading())
            savedState.get<String>("exerciseId")?.also { exerciseId ->
                exerciseRepository
                    .findById(id = exerciseId)
                    .catch { cause ->
                        emit(AppState.Loaded())
                        cause.printStackTrace()
                        emitError(cause = cause)
                    }
                    .collect { exercise ->
                        emit(AppState.Loaded())
                        emitSuccess(
                            value = ExerciseDetailState.ShowDetail(
                                detail = exercise.toView()
                            )
                        )
                    }
            } ?: run {
                emit(AppState.Loaded())
                emitError(
                    cause = IllegalStateException(), tag = ExerciseDetailState.NoSelectedExercise
                )
            }
        }
    }

    private suspend fun <T : AppState<ExerciseDetailState>> emit(value: T) =
        mutableUiState.emit(value)

    private suspend fun emitError(cause: Throwable, tag: ExerciseDetailState? = null) =
        emit(AppState.Error(cause = cause, tag = tag))

    private suspend fun <T : ExerciseDetailState> emitSuccess(value: T) =
        emit(AppState.Success(value))
}