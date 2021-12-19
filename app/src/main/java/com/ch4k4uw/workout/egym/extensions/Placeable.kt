package com.ch4k4uw.workout.egym.extensions

import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.IntSize

fun List<Placeable>.parseHorizontalSize(dimension: Int): IntSize =
    IntSize(width = dimension, height = maxByOrNull { it.height }?.height ?: 0)