package com.ch4k4uw.workout.egym.exercise.extensions

import com.ch4k4uw.workout.egym.core.exercise.domain.entity.ExerciseHead
import com.ch4k4uw.workout.egym.exercise.interaction.ExerciseHeadView

fun ExerciseHead.toView(): ExerciseHeadView =
    ExerciseHeadView(
        id = id,
        image = image,
        title = title
    )