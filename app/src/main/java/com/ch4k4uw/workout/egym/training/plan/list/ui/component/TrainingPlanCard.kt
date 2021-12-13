package com.ch4k4uw.workout.egym.training.plan.list.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.ch4k4uw.workout.egym.core.ui.AppTheme

@Composable
fun TrainingPlanCard(
    title: String = "",
    description: String = "",
    onClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .clickable(role = Role.Button, onClick = onClick),
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.Dimens.spacing.xtiny),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 1f)
                    .padding(end = AppTheme.Dimens.spacing.tiny)
            ) {
                Text(
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = AppTheme.typography.material.subtitle1,
                )
                Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.tiny))
                Text(
                    text = description,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = AppTheme.typography.material.subtitle2,
                    color = AppTheme.colors.material.onSurface.copy(
                        alpha = .5f
                    )
                )
            }
            Row {
                IconActionButton(
                    onClick = onEditClick,
                    icon = Icons.Filled.Edit
                )
                Spacer(modifier = Modifier.width(width = AppTheme.Dimens.spacing.tiny))
                IconActionButton(
                    onClick = onDeleteClick,
                    icon = Icons.Filled.Delete,
                    tint = AppTheme.colors.deepOrange
                )
            }
        }
    }
}

@Composable
private fun IconActionButton(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
) {
    val borderColor = if (AppTheme.colors.material.isLight) {
        Color.DarkGray
    } else {
        Color.LightGray
    }
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDark() {
    AppTheme {
        TrainingPlanCard(
            title = "Monday heavy chest",
            description = "Today you will do only chest exercises with every variations contained into this plan. Without excuses."
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewLight() {
    AppTheme {
        TrainingPlanCard(
            title = "Monday heavy chest",
            description = "Today you will do only chest exercises with every variations contained into this plan. Without excuses."
        )
    }
}
