package com.ch4k4uw.workout.egym.training.plan.register.ui

import android.content.res.Configuration
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.math.roundToInt

private enum class LayoutId {
    Title, Actions
}

private const val ANIMATION_DURATION = 300

@Composable
fun TrainingPlanExerciseCard(
    title: String = "",
    notes: String = "",
    sets: Int = 0,
    reps: Int = 0,
    performTip: Boolean = true,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onDetailsClick: () -> Unit = {},
) {
    var innerPerformTip by rememberSaveable { mutableStateOf(performTip) }
    var cardOffset by rememberSaveable { mutableStateOf(0f) }
    val transitionState = remember {
        MutableTransitionState(false)
    }

    val transition = updateTransition(
        transitionState = transitionState, label = "cardOffsetTransitionUpdate"
    )
    val offsetTransition by transition.animateFloat(
        label = "cardOffsetTransition",
        transitionSpec = { tween(durationMillis = ANIMATION_DURATION) },
    ) {
        if (it) -cardOffset else 0f
    }

    Layout(
        modifier = Modifier
            .clipToBounds(),
        content = {
            Surface(
                modifier = Modifier
                    .layoutId(LayoutId.Actions),
                color = AppTheme.colors.material.secondary
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    for (i in 0..2) {
                        IconButton(
                            onClick = {
                                when (i) {
                                    0 -> onEditClick()
                                    1 -> onDeleteClick()
                                    else -> onDetailsClick()
                                }
                                transitionState.targetState = false
                            }
                        ) {
                            Icon(
                                imageVector = when (i) {
                                    0 -> Icons.Filled.Edit
                                    1 -> Icons.Filled.Delete
                                    else -> Icons.Filled.Info
                                },
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            val titleColor = if (AppTheme.colors.material.isLight) {
                Color.LightGray
            } else {
                Color.DarkGray
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(offsetTransition.roundToInt(), 0) }
                    .layoutId(LayoutId.Title),
                backgroundColor = titleColor,
                contentColor = AppTheme.colors.material.onSurface.copy(
                    alpha = 0f
                ),
                shape = RectangleShape
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = AppTheme.Dimens.spacing.xtiny)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(intrinsicSize = IntrinsicSize.Min)
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(weight = 1f)
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            ExerciseTitleText(title)
                        }
                        IconButton(
                            onClick = {
                                transitionState.targetState = !transitionState.targetState
                            }
                        ) {
                            val degreesTransition = -offsetTransition / max(1f, cardOffset)
                            Icon(
                                modifier = Modifier
                                    .rotate(
                                        degrees = 180f - (180f * degreesTransition)
                                    ),
                                imageVector = Icons.Filled.DoubleArrow,
                                contentDescription = null
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        ExerciseNotesText(modifier = Modifier.weight(weight = 1f), notes = notes)
                        ExerciseSetsAndReps(sets = sets, reps = reps)
                    }
                }
            }
        }
    ) { measure, constraints ->
        val titlePlaceable = measure
            .find { it.layoutId == LayoutId.Title }
            ?.measure(constraints = constraints)
        val actionsPlaceable = measure
            .find { it.layoutId == LayoutId.Actions }
            ?.measure(
                constraints = constraints.copy(
                    minHeight = titlePlaceable?.height ?: constraints.minHeight,
                    maxHeight = titlePlaceable?.height ?: constraints.maxHeight,
                )
            )

        cardOffset = actionsPlaceable?.width?.toFloat() ?: 0f

        val wMax = max(actionsPlaceable?.width ?: 0, titlePlaceable?.width ?: 0)
        val hMax = titlePlaceable?.height ?: 0

        layout(width = wMax, height = hMax) {
            actionsPlaceable?.place(
                x = wMax - actionsPlaceable.width,
                y = 0
            )
            titlePlaceable?.place(
                x = 0,
                y = 0
            )
        }
    }

    LaunchedEffect(Unit) {
        if (innerPerformTip) {
            transitionState.targetState = true
            delay(timeMillis = ANIMATION_DURATION.toLong())
            transitionState.targetState = false
            innerPerformTip = false
        }
    }
}

@Composable
private fun ExerciseTitleText(title: String) {
    Text(
        text = title,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = AppTheme.typography.material.body1,
    )
}

@Composable
private fun ExerciseNotesText(modifier: Modifier, notes: String) {
    Text(
        modifier = modifier,
        text = notes,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = AppTheme.typography.material.caption,
    )
}

@Composable
private fun ExerciseSetsAndReps(sets: Int, reps: Int) {
    Text(
        text = stringResource(
            id = R.string.training_plan_exercise_card_sets_and_resp_label,
            sets,
            reps
        ),
        maxLines = 1,
        style = AppTheme.typography.material.body2,
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showSystemUi = true)
@Composable
private fun PreviewTrainingPlanExerciseCardDark() {
    AppTheme {
        Surface(
            color = AppTheme.colors.material.background
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TrainingPlanExerciseCard(
                    title = "Barbell Bench Press",
                    notes = "Chest exercise",
                    sets = 3,
                    reps = 12
                )
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewTrainingPlanExerciseCardLight() {
    AppTheme {
        Surface(
            color = AppTheme.colors.material.background
        ) {
            TrainingPlanExerciseCard(
                title = "Barbell Bench Press",
                notes = "Chest exercise",
                sets = 3,
                reps = 12
            )
        }
    }
}