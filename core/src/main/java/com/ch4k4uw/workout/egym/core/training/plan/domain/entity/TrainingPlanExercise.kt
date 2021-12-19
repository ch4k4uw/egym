package com.ch4k4uw.workout.egym.core.training.plan.domain.entity

data class TrainingPlanExercise(
    val exercise: String,
    val title: String,
    val notes: String,
    val sets: Int,
    val reps: Int
)
