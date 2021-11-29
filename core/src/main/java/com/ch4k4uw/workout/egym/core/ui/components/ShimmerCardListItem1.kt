package com.ch4k4uw.workout.egym.core.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
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
fun ShimmerCardListItem1(
    colors: List<Color>,
    xShimmer: Float,
    yShimmer: Float,
    cardHeight: Dp,
    gradientWidth: Float,
    padding: Dp
) {
    val brush = linearGradient(
        colors,
        start = Offset(xShimmer - gradientWidth, yShimmer - gradientWidth),
        end = Offset(xShimmer, yShimmer)
    )
    Column(modifier = Modifier.padding(padding)) {
        Surface(
            shape = AppTheme.shapes.material.small,
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight)
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
                    .height(cardHeight / 10)
                    .background(brush = brush)
            )
        }
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