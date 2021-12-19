package com.ch4k4uw.workout.egym.core.training.plan.infra.entity

import com.ch4k4uw.workout.egym.core.extensions.asLocalDateTime
import com.ch4k4uw.workout.egym.core.training.plan.domain.entity.TrainingPlan

internal data class TrainingPlanRemote(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var exercises: List<TrainingPlanExerciseRemote> = listOf(),
    var created: Long = 0,
    var updated: Long = 0
)

internal fun TrainingPlanRemote.toDomain(): TrainingPlan =
    TrainingPlan(
        id = id,
        title = title,
        description = description,
        exercises = exercises.map { it.toDomain() },
        created = created.asLocalDateTime,
        updated = updated.asLocalDateTime,
    )