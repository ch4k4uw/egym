package com.ch4k4uw.workout.egym.training.plan.register.interaction

import android.os.Parcelable
import com.ch4k4uw.workout.egym.core.training.plan.domain.entity.TrainingPlanExercise
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

fun TrainingPlanExerciseView.toDomain(): TrainingPlanExercise =
    TrainingPlanExercise(
        exercise = exercise,
        title = title,
        notes = notes,
        sets = sets,
        reps = reps
    )