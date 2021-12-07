package com.ch4k4uw.workout.egym.core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.core.ui.color.ColorConstants
import com.ch4k4uw.workout.egym.core.ui.dimens.DimensConstants
import com.ch4k4uw.workout.egym.core.ui.shape.ShapeConstants
import com.ch4k4uw.workout.egym.core.ui.typography.TypographyConstants
import com.google.accompanist.insets.ProvideWindowInsets

@ExperimentalUnitApi
private val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(material = TypographyConstants.Normal.material)
}

private val LocalAppShapes = staticCompositionLocalOf {
    AppShapes(material = ShapeConstants.Normal.material)
}

private val LocalAppColors = staticCompositionLocalOf<AppColors> {
    TODO("Undefined")
}

private val LocalAppShapeCornerDimens = staticCompositionLocalOf<AppDimens.ShapeCorner> {
    TODO("Undefined")
}

private val LocalAppSpacingDimens = staticCompositionLocalOf<AppDimens.Spacing> {
    TODO("Undefined")
}

private val LocalAppSizingDimens = staticCompositionLocalOf<AppDimens.Sizing> {
    TODO("Undefined")
}

private val LocalAppPaddingDimens = staticCompositionLocalOf<AppDimens.Padding> {
    TODO("Undefined")
}

@ExperimentalUnitApi
@Composable
fun AppTheme(
    isDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val appShapeCornerDimens = DimensConstants.Shape.normal
    val appSpacingDimens = DimensConstants.Spacing.normal
    val appSizingDimens = DimensConstants.Sizing.normal
    val appPaddingDimens = DimensConstants.Padding.normal

    val colors = if (isDark) {
        ColorConstants.DarkColors
    } else {
        ColorConstants.LightColors
    }

    val typography = TypographyConstants.Normal
    val shapes = ShapeConstants.Normal

    CompositionLocalProvider(
        LocalAppShapeCornerDimens provides appShapeCornerDimens,
        LocalAppSpacingDimens provides appSpacingDimens,
        LocalAppSizingDimens provides appSizingDimens,
        LocalAppPaddingDimens provides appPaddingDimens,
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        LocalAppShapes provides shapes
    ) {
        MaterialTheme(
            colors = colors.material,
            typography = typography.material,
            shapes = shapes.material
        ) {
            ProvideWindowInsets {
                content()
            }
        }
    }
}

object AppTheme {
    object Dimens {
        val shapeCorner: AppDimens.ShapeCorner
            @ReadOnlyComposable
            @Composable
            get() = LocalAppShapeCornerDimens.current

        val spacing: AppDimens.Spacing
            @ReadOnlyComposable
            @Composable
            get() = LocalAppSpacingDimens.current

        val sizing: AppDimens.Sizing
            @ReadOnlyComposable
            @Composable
            get() = LocalAppSizingDimens.current

        val padding: AppDimens.Padding
            @ReadOnlyComposable
            @Composable
            get() = LocalAppPaddingDimens.current
    }

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