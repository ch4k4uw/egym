package com.ch4k4uw.workout.egym.core.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.ch4k4uw.workout.egym.core.ui.AppTheme

@Composable
fun ShimmerRectangle1(
    layoutId: Any? = null,
    width: Dp = Dp.Unspecified,
    height: Dp = ShimmerRectangle1Defaults.height,
    cornerRadius: Dp = ShimmerRectangle1Defaults.cornerRadius
) {
    val animationState: ShimmerRectangle1AnimationState = rememberShimmerRectangle1Animation()
    var widthPx by remember { mutableStateOf(0) }
    var heightPx by remember { mutableStateOf(0) }
    val gradientWidth by remember {
        derivedStateOf { .4f * heightPx }
    }
    val isLight = AppTheme.colors.material.isLight
    val colors = Array(3) {
        if (isLight) {
            Color.LightGray.copy(alpha = if (it == 1) .3f else .9f)
        } else {
            Color.DarkGray.copy(alpha = if (it == 1) .3f else .9f)
        }
    }.toList()
    val x1 = -gradientWidth + ((widthPx + gradientWidth) * animationState.transition.value)
    val x2 = (widthPx + gradientWidth) * animationState.transition.value
    val y1 = -gradientWidth + ((heightPx + gradientWidth) * animationState.transition.value)
    val y2 = (heightPx + gradientWidth) * animationState.transition.value
    val brush = linearGradient(
        colors,
        start = Offset(
            x = x1,
            y = y1
        ),
        end = Offset(
            x = x2,
            y = y2
        )
    )
    Layout(
        modifier = Modifier.let { if (layoutId != null) it.layoutId(layoutId = layoutId) else it },
        content = {
            Surface(
                shape = RoundedCornerShape(size = cornerRadius)
            ) {
                Spacer(
                    modifier = Modifier
                        .height(height = height)
                        .let {
                            if (width != Dp.Unspecified) {
                                it.width(width = width)
                            } else {
                                it.fillMaxWidth()
                            }
                        }
                        .background(brush = brush)
                )
            }
        }
    ) { measure, constraints ->
        val placeable = measure[0].measure(constraints = constraints)
        widthPx = placeable.width
        heightPx = placeable.height

        layout(width = widthPx, height = heightPx) {
            placeable.place(x = 0, y = 0)
        }
    }
}

object ShimmerRectangle1Defaults {
    val height = 10.dp
    val cornerRadius = 3.dp
}

interface ShimmerRectangle1AnimationState {
    @get:Composable
    val transition: State<Float>
}

@Stable
private class ShimmerRectangle1AnimationStateImpl(
    private val infiniteTransition: InfiniteTransition
) : ShimmerRectangle1AnimationState {
    private lateinit var mTransition: State<Float>
    override val transition: State<Float>
        @Composable
        get() {
            return if (::mTransition.isInitialized) {
                mTransition
            } else {
                infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 1000,
                            easing = LinearEasing,
                            delayMillis = 300
                        ),
                        repeatMode = RepeatMode.Restart
                    )
                ).also { mTransition = it }
            }
        }
}

@Composable
fun rememberShimmerRectangle1Animation(
    infiniteTransition: InfiniteTransition = rememberInfiniteTransition()
): ShimmerRectangle1AnimationState =
        ShimmerRectangle1AnimationStateImpl(
            infiniteTransition = infiniteTransition
        )

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDark() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize(1f)
                .background(color = AppTheme.colors.material.background)
        ) {
            ShimmerRectangle1()
        }
    }
}