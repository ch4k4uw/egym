package com.ch4k4uw.workout.egym.core.exercise.domain.entity

import java.time.LocalDateTime

data class ExerciseHead(
    val id: String,
    val title : String,
    val image: String,
    val created: LocalDateTime = LocalDateTime.now(),
    val updated: LocalDateTime = LocalDateTime.now()
)
