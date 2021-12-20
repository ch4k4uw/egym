package com.ch4k4uw.workout.egym.core.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.ch4k4uw.workout.egym.core.ui.AppTheme

@Composable
fun ShimmerCardListItem2(
    padding: Dp = 0.dp,
    imageHeightScale: Float = .7f,
    onSideEffect: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val imageHeight = maxWidth * imageHeightScale
        val cardWidthPx = with(LocalDensity.current) { maxWidth.toPx() }
        val cardHeightPx = with(LocalDensity.current) { imageHeight.toPx() }
        val gradientWidth: Float = (0.2f * cardHeightPx)

        val infiniteTransition = rememberInfiniteTransition()
        val xCardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (cardWidthPx + gradientWidth),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delayMillis = 300
                ),
                repeatMode = RepeatMode.Restart
            )
        )
        val yCardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (cardHeightPx + gradientWidth),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delayMillis = 300
                ),
                repeatMode = RepeatMode.Restart
            )
        )

        val shimmerColor = AppTheme.colors.shimmer
        val colors = Array(3) {
            shimmerColor.copy(alpha = if (it == 1) .3f else .9f)
        }.toList()

        val brush = linearGradient(
            colors,
            start = Offset(xCardShimmer.value - gradientWidth, yCardShimmer.value - gradientWidth),
            end = Offset(xCardShimmer.value, yCardShimmer.value)
        )
        Column(modifier = Modifier.padding(padding)) {
            Surface(
                shape = AppTheme.shapes.material.small,
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .background(brush = brush)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                shape = AppTheme.shapes.material.small,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight / 10)
                        .background(brush = brush)
                )
            }
        }
    }
    SideEffect {
        onSideEffect()
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewShimmerCardListItem1Dark() {
    AppTheme {
        val colors = listOf(
            Color.DarkGray.copy(alpha = .9f),
            Color.DarkGray.copy(alpha = .3f),
            Color.DarkGray.copy(alpha = .9f),
        )
        ShimmerCardListItem1(
            colors = colors,
            xShimmer = 190f,
            yShimmer = 190f,
            cardHeight = 300.dp,
            gradientWidth = with(LocalDensity.current) { 20.dp.toPx() },
            padding = 0.dp
        )
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewShimmerCardListItem1Light() {
    AppTheme {
        val colors = listOf(
            Color.LightGray.copy(alpha = .9f),
            Color.LightGray.copy(alpha = .3f),
            Color.LightGray.copy(alpha = .9f),
        )
        ShimmerCardListItem1(
            colors = colors,
            xShimmer = 50f,
            yShimmer = 50f,
            cardHeight = 300.dp,
            gradientWidth = with(LocalDensity.current) { 20.dp.toPx() },
            padding = 0.dp
        )
    }
}