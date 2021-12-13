
package com.ch4k4uw.workout.egym.core.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.ch4k4uw.workout.egym.core.ui.AppTheme

@ExperimentalUnitApi
@Composable
fun SocialMediaButton(
    modifier: Modifier = Modifier,
    content: String,
    @DrawableRes icon: Int,
    iconDescription: String = "",
    iconBackgroundColor: Color = SocialMediaButtonDefaults.iconBackgroundColor,
    contentTextStyle: TextStyle = AppTheme.typography.material.subtitle2,
    contentHorizontalPadding: Dp = SocialMediaButtonDefaults.contentHorizontalPadding,
    onClick: () -> Unit
) {
    fun modifier(): Modifier =
        modifier
            .takeIf { it == Modifier }
            ?.let { Modifier.height(height = SocialMediaButtonDefaults.height) }
            ?: run { modifier }

    Surface(
        modifier = modifier()
            .clickable(
                onClick = onClick
            )
            .let {
                if (AppTheme.colors.material.isLight) {
                    it.shadow(elevation = 3.dp)
                } else {
                    it
                }
            }
            .width(IntrinsicSize.Max)
            .height(IntrinsicSize.Min),
        color = AppTheme.colors.googleButton,
        border = BorderStroke(
            width = 1.dp,
            color = if (AppTheme.colors.material.isLight) {
                Color.Black
            } else {
                Color.White
            }.copy(
                alpha = .1f
            )
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(1.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(ratio = 1f, matchHeightConstraintsFirst = true),
                color = iconBackgroundColor
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = iconDescription,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .wrapContentSize()
                )
            }
            Text(
                text = content,
                textAlign = TextAlign.Center,
                color = AppTheme.colors.material.onSurface,
                style = contentTextStyle,
                modifier = Modifier
                    .padding(horizontal = contentHorizontalPadding)
                    .weight(1f)
            )
        }
    }
}

object SocialMediaButtonDefaults {
    val contentHorizontalPadding = 8.dp
    val height = 40.dp
    val iconBackgroundColor: Color = Color.Transparent
}