package com.ch4k4uw.workout.egym.core.ui

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

sealed class AppDimens {
    @Stable
    data class Font(
        val h1: TextUnit,
        val h2: TextUnit,
        val h3: TextUnit,
        val h4: TextUnit,
        val h5: TextUnit,
        val h6: TextUnit,
        val subtitle1: TextUnit,
        val subtitle2: TextUnit,
        val body1: TextUnit,
        val body2: TextUnit,
        val button: TextUnit,
        val caption: TextUnit,
        val overLine: TextUnit,
    ) : AppDimens()
    @Stable
    data class LetterSpacing (
        val h1: TextUnit,
        val h2: TextUnit,
        val h3: TextUnit,
        val h4: TextUnit,
        val h5: TextUnit,
        val h6: TextUnit,
        val subtitle1: TextUnit,
        val subtitle2: TextUnit,
        val body1: TextUnit,
        val body2: TextUnit,
        val button: TextUnit,
        val caption: TextUnit,
        val overLine: TextUnit,
    ) : AppDimens()
    @Stable
    data class ShapeCorner(
        val small: Dp,
        val medium: Dp,
        val large: Dp
    ) : AppDimens()
}