package com.ch4k4uw.workout.egym.core.exercise.infra.data

import com.google.gson.annotations.SerializedName

data class CollectionCountRemote(
    @SerializedName("count") val count: Int,
)
