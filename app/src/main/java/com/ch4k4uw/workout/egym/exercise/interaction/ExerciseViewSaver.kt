package com.ch4k4uw.workout.egym.exercise.interaction

import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import com.ch4k4uw.workout.egym.exercise.detail.interaction.ExerciseView

object ExerciseViewSaver {
    val Saver: Saver<MutableState<ExerciseView>, *> = Saver(
        save = { Bundle().apply { putParcelable("exv", it.value) } },
        restore = { mutableStateOf(it.getParcelable("exv") ?: ExerciseView.Empty) }
    )
}