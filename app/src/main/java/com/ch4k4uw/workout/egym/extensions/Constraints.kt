package com.ch4k4uw.workout.egym.extensions

import androidx.compose.ui.unit.Constraints

val Constraints.horizontalDimension: Int get() = maxWidth

val Constraints.horizontalLooseConstraints: Constraints get() = copy(
    minHeight = 0,
    minWidth = horizontalDimension,
    maxWidth = horizontalDimension
)