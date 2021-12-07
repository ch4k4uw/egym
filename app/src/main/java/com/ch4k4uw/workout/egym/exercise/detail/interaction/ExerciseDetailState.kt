package com.ch4k4uw.workout.egym.exercise.detail.interaction

sealed class ExerciseDetailState {
    object NoSelectedExercise : ExerciseDetailState()
    data class ShowDetail(val detail: ExerciseView): ExerciseDetailState()
}
