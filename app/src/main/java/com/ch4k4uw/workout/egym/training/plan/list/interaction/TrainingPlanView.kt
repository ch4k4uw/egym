package com.ch4k4uw.workout.egym.training.plan.list.interaction

import android.os.Parcelable
import com.ch4k4uw.workout.egym.training.plan.register.interaction.TrainingPlanExerciseView
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainingPlanView(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val exercises: List<TrainingPlanExerciseView> = listOf()
) : Parcelable {
    companion object {
        val Empty = TrainingPlanView()
    }
}
