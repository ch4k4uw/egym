package com.ch4k4uw.workout.egym.training.plan.list.interaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrainingPlanView(
    val id: String = "",
    val title: String = "",
    val description: String = ""
) : Parcelable {
    companion object {
        val Empty = TrainingPlanView()
    }
}
