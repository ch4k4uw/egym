package com.ch4k4uw.workout.egym.common.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.ch4k4uw.workout.egym.common.ui.theme.dimens.DimensConstants

private val LocalEGymExerciseHeadCardDimens =
    staticCompositionLocalOf<EGymDimens.ExerciseHeadCard> {
        TODO("Undefined")
    }

@Composable
fun EGymTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalEGymExerciseHeadCardDimens provides DimensConstants.ExerciseHeadCard.normal,
    ) {
        content()
    }
}

object EGymTheme {
    object Dimens {
        val exerciseHeadCard: EGymDimens.ExerciseHeadCard
            @ReadOnlyComposable
            @Composable
            get() = LocalEGymExerciseHeadCardDimens.current
    }
}