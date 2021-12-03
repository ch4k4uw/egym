package com.ch4k4uw.workout.egym.common.ui.theme.dimens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ch4k4uw.workout.egym.common.ui.theme.EGymDimens

object DimensConstants {
    object ProfileDialog {
        val normal = EGymDimens.ProfileDialog(
            bodyBottomPadding = 4.dp,
            dataStartPadding = 16.dp,
            logoutButtonHorizontalPadding = 24.dp,
            logoutButtonBottomPadding = 24.dp,
            logoutButtonIconPadding = 8.dp
        )
    }

    object ExerciseHeadCard {
        val normal = EGymDimens.ExerciseHeadCard(
            imageHeight = .7f,
            cardElevation = 3.dp,
            titleMarginTop = 4.dp,
            cardMarginBottom = 8.dp
        )
    }

    object ExerciseListTopAppBar {
        val normal = EGymDimens.ExerciseListTopAppBar(
            profileIconPadding = 4.dp
        )
    }

    object ExerciseListTagChip {
        val normal = EGymDimens.ExerciseListTagChip(
            buttonEndPadding = 8.dp,
            elevation = 8.dp,
            textPadding = 8.dp,
        )
    }

    object ExerciseListTopTagChipBarSlot {
        val normal = EGymDimens.ExerciseListTopTagChipBarSlot(
            padding = 8.dp,
            crossAxisSpacing = 12.dp,
            mainAxisSpacing = 8.dp,
        )
    }
}