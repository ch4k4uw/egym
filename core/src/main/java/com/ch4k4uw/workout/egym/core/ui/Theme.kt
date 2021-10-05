package com.ch4k4uw.workout.egym.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.core.ui.color.ColorConstants
import com.ch4k4uw.workout.egym.core.ui.shape.ShapeConstants
import com.ch4k4uw.workout.egym.core.ui.typography.TypographyConstants

@ExperimentalUnitApi
val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(material = TypographyConstants.Normal.material)
}
val LocalAppShapes = staticCompositionLocalOf {
    AppShapes(material = ShapeConstants.Normal.material)
}
val LocalAppColors = staticCompositionLocalOf<AppColors> {
    TODO("Undefined")
}

@ExperimentalUnitApi
@Composable
fun AppTheme(
    isLight: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = AppColors(
        material = if (isLight) ColorConstants.LightColors else ColorConstants.DarkColors
    )
    val typography = AppTypography(
        material = TypographyConstants.Normal.material
    )
    val shapes = AppShapes(
        material = ShapeConstants.Normal.material
    )
    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        LocalAppShapes provides shapes
    ) {
        MaterialTheme(
            colors = colors.material,
            typography = typography.material,
            shapes = shapes.material
        ) {
            content()
        }
    }
}

object AppTheme {
    val colors: AppColors
        @ReadOnlyComposable
        @Composable
        get() = LocalAppColors.current

    @ExperimentalUnitApi
    val typography: AppTypography
        @ReadOnlyComposable
        @Composable
        get() = LocalAppTypography.current

    val shapes: AppShapes
        @ReadOnlyComposable
        @Composable
        get() = LocalAppShapes.current
}