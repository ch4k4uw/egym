package com.ch4k4uw.workout.egym.common.ui.theme

sealed class EGymDimens {
    data class ExerciseHeadCard(
        val imageHeight: Float = .7f,
    ) : EGymDimens()
}