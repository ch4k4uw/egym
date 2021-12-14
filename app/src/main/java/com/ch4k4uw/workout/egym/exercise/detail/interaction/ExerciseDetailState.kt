package com.ch4k4uw.workout.egym.exercise.detail.interaction

import java.io.Serializable

sealed class ExerciseDetailState : Serializable {
    object NoSelectedExercise : ExerciseDetailState()
    data class ShowDetail(val detail: ExerciseView) : ExerciseDetailState()
}
