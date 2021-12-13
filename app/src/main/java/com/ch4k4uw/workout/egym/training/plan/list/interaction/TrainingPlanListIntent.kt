package com.ch4k4uw.workout.egym.training.plan.list.interaction

sealed class TrainingPlanListIntent {
    object FetchUserData : TrainingPlanListIntent()
    object PerformLogout : TrainingPlanListIntent()
    object FetchPlanList : TrainingPlanListIntent()
    data class DeletePlan(val plan: TrainingPlanView, val confirmed: Boolean = false) :
        TrainingPlanListIntent()
}
