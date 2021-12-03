package com.ch4k4uw.workout.egym.common.ui.theme

import androidx.compose.ui.unit.Dp

sealed class EGymDimens {
    data class ProfileDialog(
        val bodyBottomPadding: Dp,
        val dataStartPadding: Dp,
        val logoutButtonHorizontalPadding: Dp,
        val logoutButtonBottomPadding: Dp,
        val logoutButtonIconPadding: Dp
    ) : EGymDimens()

    data class ExerciseHeadCard(
        val imageHeight: Float,
        val cardElevation: Dp,
        val titleMarginTop: Dp,
        val cardMarginBottom: Dp
    ) : EGymDimens()

    data class ExerciseListTopAppBar(
        val profileIconPadding: Dp
    ) : EGymDimens()

    data class ExerciseListTagChip(
        val buttonEndPadding: Dp,
        val elevation: Dp,
        val textPadding: Dp
    ) : EGymDimens()

    data class ExerciseListTopTagChipBarSlot(
        val padding: Dp,
        val crossAxisSpacing: Dp,
        val mainAxisSpacing: Dp
    ) : EGymDimens()
}