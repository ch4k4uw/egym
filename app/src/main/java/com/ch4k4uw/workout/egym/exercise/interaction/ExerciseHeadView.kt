package com.ch4k4uw.workout.egym.exercise.interaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseHeadView(
    val id: String,
    val image: String,
    val title: String
) : Parcelable

