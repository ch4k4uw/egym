package com.ch4k4uw.workout.egym.core.ui.dimens

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ch4k4uw.workout.egym.core.ui.AppDimens

internal object DimensConstants {
    object Font {
        val normal = AppDimens.Font(
            h1 = 96.sp,
            h2 = 60.sp,
            h3 = 48.sp,
            h4 = 34.sp,
            h5 = 24.sp,
            h6 = 20.sp,
            subtitle1 = 16.sp,
            subtitle2 = 14.sp,
            body1 = 14.sp,
            body2 = 12.sp,
            button = 12.sp,
            caption = 10.sp,
            overLine = 9.sp,
        )
    }
    object LetterSpacing {
        val normal = AppDimens.LetterSpacing(
            h1 = (-1.5f).sp,
            h2 = (-0.5f).sp,
            h3 = 0.sp,
            h4 = 0.25f.sp,
            h5 = 0.sp,
            h6 = 0.15.sp,
            subtitle1 = 0.15.sp,
            subtitle2 = 0.1.sp,
            body1 = 0.5.sp,
            body2 = 0.25.sp,
            button = 1.25.sp,
            caption = 0.4.sp,
            overLine = 1.5.sp,
        )
    }
    object Shape {
        val normal = AppDimens.ShapeCorner(
            small = 4.dp,
            medium = 4.dp,
            large = 0.dp
        )
    }
}