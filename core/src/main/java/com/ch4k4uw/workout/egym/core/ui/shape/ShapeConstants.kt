package com.ch4k4uw.workout.egym.core.ui.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import com.ch4k4uw.workout.egym.core.ui.dimens.DimensConstants

internal object ShapeConstants {
    object Normal {
        val material = Shapes(
            small = RoundedCornerShape(size = DimensConstants.Shape.normal.small),
            medium = RoundedCornerShape(size = DimensConstants.Shape.normal.medium),
            large = RoundedCornerShape(size = DimensConstants.Shape.normal.large),
        )
    }
}