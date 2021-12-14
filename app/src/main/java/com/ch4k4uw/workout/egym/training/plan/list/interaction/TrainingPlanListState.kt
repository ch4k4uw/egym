package com.ch4k4uw.workout.egym.training.plan.list.interaction

import com.ch4k4uw.workout.egym.login.interaction.UserView
import java.io.Serializable

sealed class TrainingPlanListState : Serializable {
    object ShowLoginScreen : TrainingPlanListState()
    data class DisplayUserData(val user: UserView) : TrainingPlanListState()
    data class ShowPlanList(val plans: List<TrainingPlanView>) : TrainingPlanListState()
    object DisplayNoPlansToShowMessage : TrainingPlanListState()
    data class ConfirmPlanDeletion(val plan: TrainingPlanView) : TrainingPlanListState()
    object FetchUserDataTag : TrainingPlanListState()
    object FetchPlanListTag : TrainingPlanListState()
    object PerformLogoutTag : TrainingPlanListState()
    object DeletePlanTag : TrainingPlanListState()
}
