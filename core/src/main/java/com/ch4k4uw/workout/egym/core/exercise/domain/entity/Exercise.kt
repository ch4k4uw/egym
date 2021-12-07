package com.ch4k4uw.workout.egym.core.exercise.domain.entity

import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import java.time.LocalDateTime

data class Exercise(
    val id: String,
    val title : String,
    val description : String,
    val tags: List<ExerciseTag>,
    val images: List<String>,
    val created: LocalDateTime = LocalDateTime.now(),
    val updated: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        val Empty = Exercise(
            id = "",
            title = "",
            description = "",
            tags = listOf(),
            images = listOf(),
            created = LocalDateTime.MIN,
            updated = LocalDateTime.MIN
        )
    }
}
