package com.ch4k4uw.workout.egym.core.training.plan.infra.entity

import com.ch4k4uw.workout.egym.core.training.plan.domain.entity.TrainingPlanExercise

internal data class TrainingPlanExerciseRemote(
    var exercise: String = "",
    var title: String = "",
    var notes: String = "",
    var sets: Int = 0,
    var reps: Int = 0
)

internal fun TrainingPlanExerciseRemote.toDomain(): TrainingPlanExercise =
    TrainingPlanExercise(
        exercise = exercise,
        title = title,
        notes = notes,
        sets = sets,
        reps = reps
    )