package com.ch4k4uw.workout.egym.training.plan.list.interaction

import android.os.Parcelable
import com.ch4k4uw.workout.egym.core.training.plan.domain.entity.TrainingPlan
import com.ch4k4uw.workout.egym.training.plan.register.interaction.TrainingPlanExerciseView
import com.ch4k4uw.workout.egym.training.plan.register.interaction.toDomain
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

fun TrainingPlanView.toCreateDomain(): TrainingPlan =
    TrainingPlan(
        title = title,
        description = description,
        exercises = exercises.map { it.toDomain() }
    )

fun TrainingPlanView.toUpdateDomain(): TrainingPlan =
    TrainingPlan(
        id = id,
        title = title,
        description = description,
        exercises = exercises.map { it.toDomain() }
    )