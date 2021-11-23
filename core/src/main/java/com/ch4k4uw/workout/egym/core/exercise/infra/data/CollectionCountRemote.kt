package com.ch4k4uw.workout.egym.core.common.infra.data

import com.google.gson.annotations.SerializedName

data class CollectionCountRemote(
    @SerializedName("count") val count: Int,
)
