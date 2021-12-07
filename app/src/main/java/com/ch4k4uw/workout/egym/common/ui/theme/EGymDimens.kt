package com.ch4k4uw.workout.egym.common.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

sealed class EGymDimens {
    data class ProfileDialog(
        val bodyBottomPadding: Dp = 4.dp,
        val dataStartPadding: Dp = 16.dp,
        val logoutButtonHorizontalPadding: Dp = 24.dp,
        val logoutButtonBottomPadding: Dp = 24.dp,
        val logoutButtonIconPadding: Dp = 8.dp
    ) : EGymDimens()

    data class ExerciseHeadCard(
        val imageHeight: Float = .7f,
        val cardElevation: Dp = 3.dp,
        val titleMarginTop: Dp = 4.dp,
        val cardMarginBottom: Dp = 8.dp
    ) : EGymDimens()

    data class ExerciseListTopAppBar(
        val profileIconPadding: Dp = 4.dp
    ) : EGymDimens()

    data class ExerciseListTagChip(
        val buttonEndPadding: Dp = 8.dp,
        val elevation: Dp = 8.dp,
        val textPadding: Dp = 8.dp
    ) : EGymDimens()

    data class ExerciseListTopTagChipBarSlot(
        val padding: Dp = 8.dp,
        val crossAxisSpacing: Dp = 12.dp,
        val mainAxisSpacing: Dp = 8.dp
    ) : EGymDimens()

    data class ExerciseDetail(
        val descriptionPadding: Dp = 8.dp,
        val descriptionDescriptionBottomPadding: Dp = 16.dp,
        val descriptionShimmerSmallestLineWidth: Dp = 48.dp
    )
}