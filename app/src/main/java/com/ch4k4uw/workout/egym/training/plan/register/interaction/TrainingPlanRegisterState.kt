package com.ch4k4uw.workout.egym.training.plan.register.interaction

import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanView
import java.io.Serializable

sealed class TrainingPlanRegisterState : Serializable {
    object SearchTag : TrainingPlanRegisterState()
    data class ShowPlan(val plan: TrainingPlanView) : TrainingPlanRegisterState()
    data class ShowExerciseSuggestions(val exercises: List<TrainingPlanExerciseView>) :
        TrainingPlanRegisterState()
}
