package com.ch4k4uw.workout.egym.core.training.plan.domain.entity

import java.time.LocalDateTime

data class TrainingPlan(
    val id: String,
    val title: String,
    val description: String,
    val created: LocalDateTime = LocalDateTime.now(),
    val updated: LocalDateTime = created
) {
    companion object {
        val Empty = TrainingPlan(
            id = "",
            title = "",
            description = "",
            created = LocalDateTime.MIN,
            updated = LocalDateTime.MIN
        )
    }

    constructor(title: String, description: String) : this(
        id = "",
        title = title,
        description = description
    )
}
