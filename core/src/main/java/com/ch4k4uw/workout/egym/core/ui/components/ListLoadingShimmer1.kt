package com.ch4k4uw.workout.egym.core.ui.components

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.ch4k4uw.workout.egym.core.ui.AppTheme

@Composable
fun ListLoadingShimmer1() {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val imageHeight = with(LocalDensity.current) { maxWidth * .7f }
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

        val isLight = AppTheme.colors.material.isLight
        val colors = Array(3) {
            if(isLight)  {
                Color.LightGray.copy(alpha = if (it == 1) .3f else .9f)
            } else {
                Color.DarkGray.copy(alpha = if (it == 1) .3f else .9f)
            }
        }.toList()

        LazyColumn {
            items(3){
                ShimmerCardListItem1(
                    colors = colors,
                    xShimmer = xCardShimmer.value,
                    yShimmer = yCardShimmer.value,
                    cardHeight = imageHeight,
                    gradientWidth = gradientWidth,
                    padding = 0.dp
                )
            }
        }
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewListLoadingShimmer1Dark() {
    AppTheme {
        ListLoadingShimmer1()
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewListLoadingShimmer1Light() {
    AppTheme {
        ListLoadingShimmer1()
    }
}