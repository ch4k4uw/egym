package com.ch4k4uw.workout.egym.core.ui.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import com.ch4k4uw.workout.egym.core.ui.AppShapes
import com.ch4k4uw.workout.egym.core.ui.dimens.DimensConstants

internal object ShapeConstants {
    val Normal: AppShapes by lazy {
        AppShapes(
            material = Shapes(
                small = RoundedCornerShape(size = DimensConstants.Shape.normal.small),
                medium = RoundedCornerShape(size = DimensConstants.Shape.normal.medium),
                large = RoundedCornerShape(size = DimensConstants.Shape.normal.large),
            )
        )
    }
}