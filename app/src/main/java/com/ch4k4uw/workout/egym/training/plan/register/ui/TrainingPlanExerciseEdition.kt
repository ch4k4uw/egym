package com.ch4k4uw.workout.egym.training.plan.register.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.ch4k4uw.workout.egym.BuildConfig
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.training.plan.register.interaction.TrainingPlanExerciseEditionState
import com.ch4k4uw.workout.egym.training.plan.register.interaction.rememberSaveableTrainingPlanExerciseEditionState
import java.lang.Integer.max

private object TrainingPlanExerciseEditionDefaults {
    val MaxNotesLength = if (BuildConfig.DEBUG) {
        125
    } else {
        1024
    }
}

@Composable
fun TrainingPlanExerciseEdition(
    state: TrainingPlanExerciseEditionState = rememberSaveableTrainingPlanExerciseEditionState(),
    onConfirmClick: (notes: String, sets: Int, reps: Int) -> Unit = { _, _, _ -> },
    onCloseClick: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var notesState by rememberSaveable(state.uuid.value) {
        mutableStateOf(state.notes.value)
    }
    var setsState by rememberSaveable(state.uuid.value) {
        mutableStateOf(state.sets.value.toString())
    }
    var repsState by rememberSaveable(state.uuid.value) {
        mutableStateOf(state.reps.value.toString())
    }

    val onSetsKillFocus = remember(setsState) {
        { setsState = if (setsState.isEmpty()) "1" else setsState }
    }
    val onRepsKillFocus = remember(repsState) {
        { repsState = if (repsState.isEmpty()) "1" else repsState }
    }

    val notesFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        IconButton(
            modifier = Modifier.padding(all = AppTheme.Dimens.spacing.xtiny),
            onClick = {
                keyboardController?.hide()
                onCloseClick()
            }
        ) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
        }

        Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xxnormal))

        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .weight(weight = 1f, fill = false)
                .padding(all = AppTheme.Dimens.spacing.xtiny)
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {
                NotesTextField(
                    notes = notesState,
                    focusRequester = notesFocusRequester,
                    onNotesChange = {
                        if (it.length < TrainingPlanExerciseEditionDefaults.MaxNotesLength) {
                            notesState = it
                        }
                    }
                )

                Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xtiny))

                Counter(
                    label = stringResource(id = R.string.training_plan_exercise_edition_sets_label),
                    value = setsState,
                    onKillFocus = onSetsKillFocus,
                    onValueChange = { setsState = it },
                    onIncreaseClick = {
                        setsState = (setsState.toIntOrNull()?.let { it + 1 } ?: 1).toString()
                    },
                    onDecreaseClick = {
                        val curr = setsState.toIntOrNull()?.let { max(1, it - 1) } ?: 1
                        setsState = curr.toString()
                    },
                    onNextActionPerform = {
                        focusManager.moveFocus(focusDirection = FocusDirection.Down)
                    }
                )

                Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xtiny))

                Counter(
                    label = stringResource(id = R.string.training_plan_exercise_edition_reps_label),
                    value = repsState,
                    onKillFocus = onRepsKillFocus,
                    onValueChange = { repsState = it },
                    onIncreaseClick = {
                        repsState = (repsState.toIntOrNull()?.let { it + 1 } ?: 1).toString()
                    },
                    onDecreaseClick = {
                        val curr = repsState.toIntOrNull()?.let { max(1, it - 1) } ?: 1
                        repsState = curr.toString()
                    },
                    onNextActionPerform = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                )

                Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xxnormal))
            }
        }
        ConfirmButton(
            label = stringResource(
                id = R.string.training_plan_exercise_edition_confirm_action_label
            ),
            onClick = {
                keyboardController?.hide()
                onSetsKillFocus()
                onRepsKillFocus()
                onConfirmClick(
                    notesState,
                    setsState.let { if (it.isEmpty()) "1" else it }.toInt(),
                    repsState.let { if (it.isEmpty()) "1" else it }.toInt()
                )
            }
        )
    }
}

@Composable
private fun NotesTextField(
    notes: String,
    focusRequester: FocusRequester,
    onNotesChange: (String) -> Unit
) {
    val height = with(LocalDensity.current) {
        LocalTextStyle.current.fontSize.toDp() * 10
    }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = height)
            .focusRequester(focusRequester = focusRequester),
        value = notes,
        onValueChange = onNotesChange,
        label = {
            Text(
                text = stringResource(id = R.string.training_plan_exercise_edition_notes_label)
            )
        },
        shape = RoundedCornerShape(size = AppTheme.Dimens.spacing.tiny),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
        )
    )
}

@Composable
private fun Counter(
    label: String,
    value: String,
    onKillFocus: () -> Unit,
    onValueChange: (String) -> Unit,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    onNextActionPerform: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onDecreaseClick) {
                Icon(imageVector = Icons.Filled.Remove, contentDescription = null)
            }
            TextField(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxHeight()
                    .onFocusChanged { state ->
                        if (!state.isFocused) onKillFocus()
                    },
                value = value,
                onValueChange = { count ->
                    onValueChange(
                        count
                            .filter { char -> char.isDigit() }
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    autoCorrect = false,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        onNextActionPerform()
                    }
                ),
                singleLine = true,
                textStyle = AppTheme.typography.material.body1.copy(
                    textAlign = TextAlign.Center
                ),
                label = {
                    Text(text = label, style = AppTheme.typography.material.caption)
                }
            )
            IconButton(onClick = onIncreaseClick) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    }
}

@Composable
fun ConfirmButton(
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(intrinsicSize = IntrinsicSize.Min)
            .padding(horizontal = AppTheme.Dimens.spacing.xtiny),
    ) {
        Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.tiny))

        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onClick,
            shape = RoundedCornerShape(percent = 50),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = AppTheme.colors.material.secondary
            )
        ) {
            Text(text = label)
        }

        Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xxnormal))
    }
}
