package com.ch4k4uw.workout.egym.exercise.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ch4k4uw.workout.egym.common.BaseAppStateViewModel
import com.ch4k4uw.workout.egym.core.exercise.domain.repository.ExerciseRepository
import com.ch4k4uw.workout.egym.exercise.detail.interaction.ExerciseDetailIntent
import com.ch4k4uw.workout.egym.exercise.detail.interaction.ExerciseDetailState
import com.ch4k4uw.workout.egym.exercise.extensions.toView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseDetailViewModel @Inject constructor(
    private val savedState: SavedStateHandle,
    private val exerciseRepository: ExerciseRepository
) : BaseAppStateViewModel<ExerciseDetailState, ExerciseDetailIntent>() {

    init {
        fetchExerciseDetails()
    }

    override fun performIntent(intent: ExerciseDetailIntent) {
        when (intent) {
            is ExerciseDetailIntent.FetchExerciseDetails -> fetchExerciseDetails()
        }
    }

    private fun fetchExerciseDetails() {
        viewModelScope.launch {
            emitLoading()
            savedState.get<String>("exerciseId")?.also { exerciseId ->
                exerciseRepository
                    .findById(id = exerciseId)
                    .catch { cause ->
                        emitLoaded()
                        emitError(cause = cause)
                        cause.printStackTrace()
                    }
                    .collect { exercise ->
                        emitLoaded()
                        emitSuccess(
                            value = ExerciseDetailState.ShowDetail(
                                detail = exercise.toView()
                            )
                        )
                    }
            } ?: run {
                emitLoaded()
                emitError(
                    cause = IllegalStateException(), tag = ExerciseDetailState.NoSelectedExercise
                )
            }
        }
    }
}