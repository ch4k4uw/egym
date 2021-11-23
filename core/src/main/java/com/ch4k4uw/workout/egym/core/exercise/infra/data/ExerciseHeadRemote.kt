package com.ch4k4uw.workout.egym.core.exercise.infra.data

import com.ch4k4uw.workout.egym.core.exercise.domain.entity.ExerciseHead
import com.ch4k4uw.workout.egym.core.extensions.asLocalDateTime
import com.google.gson.annotations.SerializedName

data class ExerciseHeadRemote(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("created") val created: Long,
    @SerializedName("updated") val updated: Long,
)

fun ExerciseHeadRemote.toDomain(): ExerciseHead =
    ExerciseHead(
        id = id,
        title = title,
        image = if (images.isEmpty()) "" else images[0],
        created = created.asLocalDateTime,
        updated = updated.asLocalDateTime
    )