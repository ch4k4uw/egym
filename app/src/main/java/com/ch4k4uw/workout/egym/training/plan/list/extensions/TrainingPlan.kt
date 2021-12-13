package com.ch4k4uw.workout.egym.training.plan.list.extensions

import com.ch4k4uw.workout.egym.core.training.plan.domain.entity.TrainingPlan
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanView

fun TrainingPlan.toView(): TrainingPlanView =
    TrainingPlanView(
        id = id,
        title = title,
        description = description
    )

fun List<TrainingPlan>.toView(): List<TrainingPlanView> =
    map { it.toView() }