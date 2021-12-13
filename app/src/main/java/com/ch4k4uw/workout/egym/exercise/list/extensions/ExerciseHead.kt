package com.ch4k4uw.workout.egym.exercise.list.extensions

import com.ch4k4uw.workout.egym.core.exercise.domain.entity.ExerciseHead
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseHeadView

fun ExerciseHead.toView(): ExerciseHeadView =
    ExerciseHeadView(
        id = id,
        image = image,
        title = title
    )

fun List<ExerciseHead>.toView(): List<ExerciseHeadView> =
    map { it.toView() }