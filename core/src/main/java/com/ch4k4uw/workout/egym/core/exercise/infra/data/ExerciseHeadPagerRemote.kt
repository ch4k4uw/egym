package com.ch4k4uw.workout.egym.core.exercise.infra.data

import com.google.gson.annotations.SerializedName

data class ExerciseHeadPagerRemote(
    @SerializedName("index") val index: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("itemsCount") val itemsCount: Int,
    @SerializedName("queryString") val queryString: String?,
    @SerializedName("queryTags") val queryTags: List<String>?,
    @SerializedName("items") val items: List<ExerciseHeadRemote>,
)
