package com.ch4k4uw.workout.egym.training.plan.register.interaction

import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanView

sealed class TrainingPlanRegisterIntent {
    data class SavePlan(val plan: TrainingPlanView) : TrainingPlanRegisterIntent()
    data class PerformExerciseQuery(val query: String) : TrainingPlanRegisterIntent()
}
