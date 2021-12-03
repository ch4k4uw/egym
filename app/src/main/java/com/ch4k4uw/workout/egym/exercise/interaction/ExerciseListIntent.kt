package com.ch4k4uw.workout.egym.exercise.interaction

import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag

sealed class ExerciseListIntent {
    object PerformLogout : ExerciseListIntent()
    object FetchNextPage : ExerciseListIntent()
    data class PerformQuery(val query: String, val tags: List<ExerciseTag>): ExerciseListIntent()
}
