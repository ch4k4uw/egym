package com.ch4k4uw.workout.egym.exercise.list.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag

val ExerciseTag.displayText: String
    @Composable
    @ReadOnlyComposable
    get() = when(this) {
        ExerciseTag.Back -> stringResource(id = R.string.exercise_tag_back_title)
        ExerciseTag.Arms -> stringResource(id = R.string.exercise_tag_arms_title)
        ExerciseTag.Shoulders -> stringResource(id = R.string.exercise_tag_shoulders_title)
        ExerciseTag.Chest -> stringResource(id = R.string.exercise_tag_chest_title)
        ExerciseTag.Legs -> stringResource(id = R.string.exercise_tag_legs_title)
        ExerciseTag.Core -> stringResource(id = R.string.exercise_tag_core_title)
        ExerciseTag.Abs -> stringResource(id = R.string.exercise_tag_abs_title)
        ExerciseTag.Bicep -> stringResource(id = R.string.exercise_tag_bicep_title)
        ExerciseTag.Tricep -> stringResource(id = R.string.exercise_tag_tricep_title)
        ExerciseTag.Cardio -> stringResource(id = R.string.exercise_tag_cardio_title)
        ExerciseTag.Aerobic -> stringResource(id = R.string.exercise_tag_aerobic_title)
        ExerciseTag.Anaerobic -> stringResource(id = R.string.exercise_tag_anaerobic_title)
        ExerciseTag.Flexibility -> stringResource(id = R.string.exercise_tag_flexibility_title)
    }