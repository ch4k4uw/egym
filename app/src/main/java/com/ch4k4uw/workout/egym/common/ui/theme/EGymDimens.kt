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
}