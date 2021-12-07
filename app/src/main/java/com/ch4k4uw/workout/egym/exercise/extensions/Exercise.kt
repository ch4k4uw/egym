package com.ch4k4uw.workout.egym.exercise.extensions

import com.ch4k4uw.workout.egym.core.exercise.domain.entity.Exercise
import com.ch4k4uw.workout.egym.exercise.detail.interaction.ExerciseView

fun Exercise.toView(): ExerciseView =
    ExerciseView(
        id = id,
        title = title,
        description = description,
        tags = tags,
        images = images
    )