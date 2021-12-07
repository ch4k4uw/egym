package com.ch4k4uw.workout.egym.exercise.list.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.common.ui.theme.EGymTheme
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.exercise.list.extensions.displayText

@Composable
fun ExerciseListTagChip(
    tag: ExerciseTag,
    isSelected: Boolean = false,
    onTagSelectionChanged: (ExerciseTag, Boolean) -> Unit = { _, _ -> }
) {
    Surface(
        modifier = Modifier.padding(end = AppTheme.Dimens.spacing.xtiny),
        elevation = AppTheme.Dimens.spacing.xtiny,
        shape = RoundedCornerShape(size = with(LocalDensity.current) {
            MaterialTheme.typography.body2.fontSize.toDp()
        }),
        color = if(isSelected)
            AppTheme.colors.material.secondaryVariant
        else
            AppTheme.colors.material.secondary
    ) {
        Row(modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onTagSelectionChanged(tag, it)
                }
            )
        ) {
            Text(
                text = tag.displayText,
                style = MaterialTheme.typography.body2,
                color = AppTheme.colors.material.onSecondary,
                modifier = Modifier.padding(all = AppTheme.Dimens.spacing.xtiny)
            )
        }
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDark() {
    AppTheme {
        EGymTheme {
            Box(modifier = Modifier.background(color = AppTheme.colors.material.primary)) {
                ExerciseListTagChip(tag = ExerciseTag.Back)
            }
        }
    }
}


@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewLight() {
    AppTheme {
        EGymTheme {
            Box(modifier = Modifier.background(color = AppTheme.colors.material.primary)) {
                ExerciseListTagChip(tag = ExerciseTag.Back)
            }
        }
    }
}