package com.ch4k4uw.workout.egym.common.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.core.ui.AppTheme

@Composable
fun EmptyContentPlaceholder(
    modifier: Modifier = Modifier,
    message: String = stringResource(id = R.string.no_content_to_show_message),
    onRefreshClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .let {
                if (modifier === Modifier)
                    modifier.fillMaxWidth()
                else
                    modifier
            }
    ) {
        Spacer(modifier = Modifier.width(AppTheme.Dimens.spacing.xtiny))
        val color = if (AppTheme.colors.material.isLight) {
            Color.DarkGray
        } else {
            Color.LightGray
        }
        Column(
            modifier = Modifier
                .weight(weight = 1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(AppTheme.Dimens.spacing.xtiny))
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(size = AppTheme.Dimens.spacing.tiny),
                color = color.copy(alpha = .3f),
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = AppTheme.Dimens.spacing.xtiny),
                    text = message,
                    textAlign = TextAlign.Center
                )
            }
            if (onRefreshClick != null) {
                val iconColor = AppTheme.colors.green
                IconButton(onClick = onRefreshClick) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = null,
                        tint = iconColor
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(AppTheme.Dimens.spacing.xtiny))
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDark() {
    AppTheme {
        Surface(
            color = AppTheme.colors.material.background
        ) {
            EmptyContentPlaceholder {

            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewLight() {
    AppTheme {
        Surface(
            color = AppTheme.colors.material.background
        ) {
            EmptyContentPlaceholder {

            }
        }
    }
}