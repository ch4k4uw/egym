package com.ch4k4uw.workout.egym.core.exercise.domain.data

data class ExercisePagerOptions(
    val size: Int
) {
    companion object {
        val Default = ExercisePagerOptions(size = 10)
    }
}
