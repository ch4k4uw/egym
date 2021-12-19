package com.ch4k4uw.workout.egym.training.plan.register.interaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainingPlanExerciseView(
    val exercise: String = "",
    val title: String = "",
    val notes: String = "",
    val sets: Int = 0,
    val reps: Int = 0
) : Parcelable {
    companion object {
        val Empty = TrainingPlanExerciseView()
    }
}
