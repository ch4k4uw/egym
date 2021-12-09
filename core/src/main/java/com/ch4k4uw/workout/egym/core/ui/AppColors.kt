package com.ch4k4uw.workout.egym.core.ui

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Stable
class AppColors(
    val material: Colors,
    val googleButton: Color,
    val amber600: Color = Color(color = 0xFFFFB300),
    val amber200: Color = Color(color = 0xFFFFE082),
    val green600: Color = Color(color = 0xFF43A047),
    val green200: Color = Color(color = 0xFFA5D6A7),
    val deepOrange600: Color = Color(color = 0xFFF4511E),
    val deepOrange200: Color = Color(color = 0xFFFFAB91),
)