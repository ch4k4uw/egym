package com.ch4k4uw.workout.egym.exercise.list.interaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class ExerciseHeadView(
    val id: String,
    val image: String,
    val title: String
) : Parcelable, Serializable

