package com.ch4k4uw.workout.egym.common.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.ch4k4uw.workout.egym.common.ui.theme.dimens.DimensConstants

private val LocalEGymProfileDialogDimens = staticCompositionLocalOf<EGymDimens.ProfileDialog> {
    TODO("Undefined")
}

private val LocalEGymExerciseHeadCardDimens =
    staticCompositionLocalOf<EGymDimens.ExerciseHeadCard> {
        TODO("Undefined")
    }

private val LocalEGymExerciseListTopAppBarDimens =
    staticCompositionLocalOf<EGymDimens.ExerciseListTopAppBar> {
        TODO("Undefined")
    }

private val LocalEGymExerciseListTagChipDimens =
    staticCompositionLocalOf<EGymDimens.ExerciseListTagChip> {
        TODO("Undefined")
    }

private val LocalEGymExerciseListTopTagChipBarSlotDimens =
    staticCompositionLocalOf<EGymDimens.ExerciseListTopTagChipBarSlot> {
        TODO("Undefined")
    }

private val LocalEGymExerciseDetailDimens =
    staticCompositionLocalOf<EGymDimens.ExerciseDetail> {
        TODO("Undefined")
    }

@Composable
fun EGymTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalEGymProfileDialogDimens provides DimensConstants.ProfileDialog.normal,
        LocalEGymExerciseHeadCardDimens provides DimensConstants.ExerciseHeadCard.normal,
        LocalEGymExerciseListTopAppBarDimens provides DimensConstants.ExerciseListTopAppBar.normal,
        LocalEGymExerciseListTagChipDimens provides DimensConstants.ExerciseListTagChip.normal,
        LocalEGymExerciseListTopTagChipBarSlotDimens provides DimensConstants
            .ExerciseListTopTagChipBarSlot.normal,
        LocalEGymExerciseDetailDimens provides DimensConstants.ExerciseDetail.normal
    ) {
        content()
    }
}

object EGymTheme {
    object Dimens {
        val profileDialog: EGymDimens.ProfileDialog
            @ReadOnlyComposable
            @Composable
            get() = LocalEGymProfileDialogDimens.current
        val exerciseHeadCard: EGymDimens.ExerciseHeadCard
            @ReadOnlyComposable
            @Composable
            get() = LocalEGymExerciseHeadCardDimens.current
        val exerciseListTopAppBar: EGymDimens.ExerciseListTopAppBar
            @ReadOnlyComposable
            @Composable
            get() = LocalEGymExerciseListTopAppBarDimens.current
        val exerciseListTagChip: EGymDimens.ExerciseListTagChip
            @ReadOnlyComposable
            @Composable
            get() = LocalEGymExerciseListTagChipDimens.current
        val exerciseListTopTagChipBarSlot: EGymDimens.ExerciseListTopTagChipBarSlot
            @ReadOnlyComposable
            @Composable
            get() = LocalEGymExerciseListTopTagChipBarSlotDimens.current
        val exerciseDetail: EGymDimens.ExerciseDetail
            @ReadOnlyComposable
            @Composable
            get() = LocalEGymExerciseDetailDimens.current
    }
}