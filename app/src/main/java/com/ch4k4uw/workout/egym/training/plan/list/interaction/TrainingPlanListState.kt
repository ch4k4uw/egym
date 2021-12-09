package com.ch4k4uw.workout.egym.training.plan.list.interaction

import com.ch4k4uw.workout.egym.core.training.plan.domain.entity.TrainingPlan

sealed class TrainingPlanListState {
    data class ShowTrainingPlanList(val plans: List<TrainingPlan>) : TrainingPlanListState()
    object DisplayNoTrainingPlansToShowMessage : TrainingPlanListState()
}
