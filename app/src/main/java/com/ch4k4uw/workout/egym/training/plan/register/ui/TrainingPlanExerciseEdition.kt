package com.ch4k4uw.workout.egym.training.plan.register.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.tooling.preview.Preview
import com.ch4k4uw.workout.egym.BuildConfig
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    notes: String = "",
    sets: Int = 0,
    reps: Int = 0,
    onConfirmClick: (notes: String, sets: Int, reps: Int) -> Unit = { _, _, _ -> },
    onCloseClick: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var notesState by rememberSaveable { mutableStateOf(notes) }
    var setsState by rememberSaveable { mutableStateOf(sets.toString()) }
    var repsState by rememberSaveable { mutableStateOf(reps.toString()) }

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

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.Dimens.spacing.xtiny)
        ) {
            item {
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
                textStyle = AppTheme.typography.material.h6.copy(
                    textAlign = TextAlign.Center
                ),
                label = {
                    Text(text = label, style = AppTheme.typography.material.body1)
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


//region preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewTrainingPlanExerciseEditionBottomSheetDark() {
    AppTheme {
        val keyboardController = LocalSoftwareKeyboardController.current
        val scope = rememberCoroutineScope()
        var expandDelayed: suspend () -> Unit by remember { mutableStateOf({}) }

        val state = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            confirmStateChange = {
                if (it == ModalBottomSheetValue.Hidden) {
                    keyboardController?.hide()
                    scope.launch {
                        expandDelayed()
                    }
                }
                true
            }
        )
        ModalBottomSheetLayout(
            modifier = Modifier
                .navigationBarsWithImePadding(),
            sheetState = state,
            sheetContent = {
                TrainingPlanExerciseEdition(
                    notes = "",
                    sets = 3,
                    reps = 12,
                    onConfirmClick = { _, _, _ ->
                        scope.launch {
                            state.hide()
                            delay(2000)
                            state.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    },
                    onCloseClick = {
                        scope.launch {
                            state.hide()
                            delay(2000)
                            state.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    }
                )
            },
            sheetShape = RoundedCornerShape(
                topStart = AppTheme.Dimens.sizing.small,
                topEnd = AppTheme.Dimens.sizing.small,
            )
        ) {

        }
        LaunchedEffect(Unit) {
            expandDelayed = {
                delay(2000)
                state.animateTo(ModalBottomSheetValue.Expanded)
            }
            state.show()
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewTrainingPlanExerciseEditionDark() {
    AppTheme {
        Surface(
            color = AppTheme.colors.material.background
        ) {
            TrainingPlanExerciseEdition(
                notes = "",
                sets = 3,
                reps = 12
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewTrainingPlanExerciseEditionLight() {
    AppTheme {
        Surface(
            color = AppTheme.colors.material.background
        ) {
            TrainingPlanExerciseEdition(
                notes = "",
                sets = 3,
                reps = 12
            )
        }
    }
}
//endregion