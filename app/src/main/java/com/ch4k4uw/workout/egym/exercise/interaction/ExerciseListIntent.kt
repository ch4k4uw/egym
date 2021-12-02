package com.ch4k4uw.workout.egym.exercise.interaction

sealed class ExerciseListIntent {
    object PerformLogout : ExerciseListIntent()
    object FetchNextPage : ExerciseListIntent()
    data class PerformQuery(val query: String): ExerciseListIntent()
}
