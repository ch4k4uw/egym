package com.ch4k4uw.workout.egym.training.plan.register.interaction

import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseHeadView
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanView
import java.io.Serializable

sealed class TrainingPlanRegisterState : Serializable {
    object SearchTag : TrainingPlanRegisterState()
    object SavePlanTag : TrainingPlanRegisterState()
    data class ShowPlan(val plan: TrainingPlanView) : TrainingPlanRegisterState()
    data class ShowExerciseSuggestions(val exercises: List<ExerciseHeadView>) :
        TrainingPlanRegisterState()

    data class ShowPlanSuccessfulSavedMessage(val plan: TrainingPlanView) :
        TrainingPlanRegisterState()
}
