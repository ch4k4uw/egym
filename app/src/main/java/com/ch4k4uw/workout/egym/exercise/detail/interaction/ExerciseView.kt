package com.ch4k4uw.workout.egym.exercise.detail.interaction

import android.os.Parcelable
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseView(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val images: List<String> = listOf(),
    val tags: List<ExerciseTag> = listOf()
) : Parcelable {
    companion object {
        val Empty = ExerciseView()
    }
}
