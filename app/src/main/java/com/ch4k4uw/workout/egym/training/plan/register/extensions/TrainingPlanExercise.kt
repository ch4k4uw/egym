package com.ch4k4uw.workout.egym.training.plan.register.extensions

import com.ch4k4uw.workout.egym.core.training.plan.domain.entity.TrainingPlanExercise
import com.ch4k4uw.workout.egym.training.plan.register.interaction.TrainingPlanExerciseView

fun TrainingPlanExercise.toView(): TrainingPlanExerciseView =
    TrainingPlanExerciseView(
        exercise = exercise,
        title = title,
        notes = notes,
        sets = sets,
        reps = reps
    )