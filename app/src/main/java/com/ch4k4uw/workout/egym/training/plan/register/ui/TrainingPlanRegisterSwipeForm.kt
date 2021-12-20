package com.ch4k4uw.workout.egym.training.plan.register.ui

import android.content.res.Configuration
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.common.ui.component.EmptyContentPlaceholder
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.extensions.horizontalDimension
import com.ch4k4uw.workout.egym.extensions.horizontalLooseConstraints
import com.ch4k4uw.workout.egym.extensions.parseHorizontalSize
import kotlinx.coroutines.delay

private object TrainingPlanRegisterSwipeForm {
    enum class LayoutId {
        First, Second, Third
    }

    const val ANIMATION_DURATION = 300

    const val MAX_DESCRIPTION_LENGTH = 1024
    const val DEFAULT_SETS = 3
    const val DEFAULT_REPS = 12
}

@Composable
fun TrainingPlanRegisterSwipeForm(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    currPage: Int,
    title: TextFieldValue = TextFieldValue(text = ""),
    description: TextFieldValue = TextFieldValue(text = ""),
    exercises: List<Pair<String, Triple<Pair<String, String>, Int, Int>>> = listOf(),
    exercisesSuggestions: List<Pair<String, String>> = listOf(),
    performExerciseTip: Boolean = false,
    onTitleChange: (TextFieldValue) -> Unit = {},
    onConfirmTitleAction: () -> Unit = {},
    onTitleFocusChange: (FocusState) -> Unit = {},
    onDescriptionChange: (TextFieldValue) -> Unit = {},
    onDescriptionFocusChange: (FocusState) -> Unit = {},
    onAddExercise: (Pair<String, Triple<Pair<String, String>, Int, Int>>) -> Unit = {},
    onEditExerciseClick: (String) -> Unit = {},
    onDetailsExerciseClick: (String) -> Unit = {},
    onDeleteExerciseClick: (String) -> Unit = {},
    onExerciseQueryChange: (String) -> Unit = {},
) {
    var pageSize by rememberSaveable { mutableStateOf(0) }
    val transitionState = remember {
        MutableTransitionState(currPage)
    }.apply { targetState = currPage }
    val transition = updateTransition(
        transitionState = transitionState, label = "cardOffsetTransitionUpdate"
    )
    val offsetTransition by transition.animateInt(
        label = "cardOffsetTransition",
        transitionSpec = {
            tween(durationMillis = TrainingPlanRegisterSwipeForm.ANIMATION_DURATION)
        },
    ) {
        -it * pageSize
    }

    val planTitleFocusRequester = remember { FocusRequester() }
    val planDescriptionFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Layout(
        modifier = Modifier
            .clipToBounds()
            .then(modifier),
        content = {
            TrainingPlanRegisterSwipeForm.LayoutId.values().map {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = contentPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        when (it) {
                            TrainingPlanRegisterSwipeForm.LayoutId.First -> {
                                PlanTitleForm(
                                    title = title,
                                    focusRequester = planTitleFocusRequester,
                                    onTextChange = onTitleChange,
                                    onFocusChanged = onTitleFocusChange,
                                    onDone = onConfirmTitleAction
                                )
                            }
                            TrainingPlanRegisterSwipeForm.LayoutId.Second -> {
                                PlanDescriptionForm(
                                    description = description,
                                    focusRequester = planDescriptionFocusRequester,
                                    onTextChange = onDescriptionChange,
                                    onFocusChanged = onDescriptionFocusChange
                                )
                            }
                            TrainingPlanRegisterSwipeForm.LayoutId.Third -> {
                                PlanExercisesForm(
                                    exercises = exercises,
                                    suggestions = exercisesSuggestions,
                                    onSearchChange = onExerciseQueryChange,
                                    performExerciseTip = performExerciseTip,
                                    onAddClick = {
                                        onAddExercise(
                                            Pair(
                                                first = it.first,
                                                second = Triple(
                                                    first = Pair(
                                                        first = it.second,
                                                        second = ""
                                                    ),
                                                    second = TrainingPlanRegisterSwipeForm
                                                        .DEFAULT_SETS,
                                                    third = TrainingPlanRegisterSwipeForm
                                                        .DEFAULT_REPS
                                                )
                                            )
                                        )
                                    },
                                    onEditClick = onEditExerciseClick,
                                    onDetailsClick = onDetailsExerciseClick,
                                    onDeleteClick = onDeleteExerciseClick
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { measurable, constraints ->
        val dimension = constraints.horizontalDimension
        val looseConstraints = constraints.horizontalLooseConstraints
        val placeableList = measurable.map { it.measure(looseConstraints) }
        val size = placeableList.parseHorizontalSize(dimension = dimension)
        pageSize = dimension
        layout(width = size.width, height = size.height) {
            for (i in TrainingPlanRegisterSwipeForm.LayoutId.values().indices) {
                val xOffset = dimension * i
                placeableList[i].place(
                    x = xOffset + offsetTransition,
                    y = 0
                )
            }
        }
    }

    LaunchedEffect(key1 = currPage) {
        when (currPage) {
            0 -> planTitleFocusRequester.requestFocus()
            1 -> planDescriptionFocusRequester.requestFocus()
            else -> focusManager.clearFocus()
        }
    }
}

@Composable
private fun PlanTitleForm(
    title: TextFieldValue,
    focusRequester: FocusRequester,
    onTextChange: (TextFieldValue) -> Unit,
    onFocusChanged: (FocusState) -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged(onFocusChanged)
                .focusRequester(focusRequester = focusRequester),
            value = title,
            label = {
                Text(
                    text = stringResource(id = R.string.training_plan_register_form_plan_title_label),
                    style = AppTheme.typography.material.body1
                )
            },
            onValueChange = {
                onTextChange(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone()
                    defaultKeyboardAction(ImeAction.Done)
                },
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(AppTheme.Dimens.spacing.tiny))
        Text(
            text = stringResource(id = R.string.training_plan_register_form_plan_title_tip),
            style = AppTheme.typography.material.subtitle2
        )
    }
}

@Composable
private fun PlanDescriptionForm(
    description: TextFieldValue,
    focusRequester: FocusRequester,
    onFocusChanged: (FocusState) -> Unit,
    onTextChange: (TextFieldValue) -> Unit
) {
    val height = with(LocalDensity.current) { (LocalTextStyle.current.fontSize * 10).toDp() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = height)
                .onFocusChanged(onFocusChanged)
                .focusRequester(focusRequester = focusRequester),
            value = description,
            label = {
                Text(
                    text = stringResource(
                        id = R.string.training_plan_register_form_plan_description_label
                    ),
                    style = AppTheme.typography.material.body1
                )
            },
            onValueChange = {
                if (it.text.length < TrainingPlanRegisterSwipeForm.MAX_DESCRIPTION_LENGTH) {
                    onTextChange(it)
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
            ),
            shape = RoundedCornerShape(size = AppTheme.Dimens.spacing.tiny),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            )
        )
        Spacer(modifier = Modifier.height(AppTheme.Dimens.spacing.tiny))
        Text(
            text = stringResource(id = R.string.training_plan_register_form_plan_description_tip),
            style = AppTheme.typography.material.subtitle2
        )
    }
}

@Composable
private fun PlanExercisesForm(
    exercises: List<Pair<String, Triple<Pair<String, String>, Int, Int>>>,
    suggestions: List<Pair<String, String>>,
    performExerciseTip: Boolean,
    onSearchChange: (String) -> Unit,
    onAddClick: (Pair<String, String>) -> Unit,
    onEditClick: (String) -> Unit,
    onDetailsClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
) {
    var selectedSuggestion by rememberSaveable { mutableStateOf(Pair("", "")) }
    var isForcingSuggestionsCollapsing by rememberSaveable { mutableStateOf(false) }
    val isSuggestionExpanded by remember(suggestions) {
        derivedStateOf {
            !isForcingSuggestionsCollapsing && suggestions.isNotEmpty() && selectedSuggestion.first.isEmpty()
        }
    }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExposedDropdownMenuBox(
                modifier = Modifier
                    .weight(weight = 1f),
                expanded = isSuggestionExpanded,
                onExpandedChange = {
                }
            ) {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = selectedSuggestion.second,
                    onValueChange = { query ->
                        val id = suggestions.takeIf { it.size == 1 }?.find {
                            it.second.lowercase() == query
                        }?.first ?: ""
                        selectedSuggestion = Pair(id, query)
                        if (id.isEmpty()) {
                            onSearchChange(query)
                        }
                    },
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = isSuggestionExpanded,
                    onDismissRequest = { isForcingSuggestionsCollapsing = true }
                ) {
                    suggestions.forEach { suggestion ->
                        DropdownMenuItem(
                            onClick = {
                                selectedSuggestion = suggestion
                                isForcingSuggestionsCollapsing = true
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier
                                        .weight(weight = 1f),
                                    text = suggestion.second,
                                    style = AppTheme.typography.material.body2
                                )
                                IconButton(
                                    onClick = {
                                        onDetailsClick(suggestion.first)
                                    }
                                ) {
                                    Icon(imageVector = Icons.Filled.Info, contentDescription = null)
                                }
                            }
                        }
                    }
                }
            }
            val addEnableState by remember(exercises) {
                derivedStateOf {
                    selectedSuggestion.first.isNotBlank() && !exercises.any {
                        it.first == selectedSuggestion.first
                    }
                }
            }
            IconButton(
                enabled = addEnableState,
                onClick = {
                    onAddClick(selectedSuggestion)
                    selectedSuggestion = Pair("", "")
                    onSearchChange("")
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.height(AppTheme.Dimens.spacing.xtiny))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f),
            backgroundColor = Color.Transparent,
            contentColor = contentColorFor(backgroundColor = AppTheme.colors.material.background),
            elevation = Dp.Hairline
        ) {
            if (exercises.isEmpty()) {
                EmptyContentPlaceholder()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    items(items = exercises, key = { it.first }) {
                        TrainingPlanExerciseCard(
                            title = it.second.first.first,
                            notes = it.second.first.second,
                            sets = it.second.second,
                            reps = it.second.third,
                            performTip = performExerciseTip,
                            onEditClick = {
                                focusManager.clearFocus()
                                onEditClick(it.first)
                            },
                            onDeleteClick = {
                                focusManager.clearFocus()
                                onDeleteClick(it.first)
                            },
                            onDetailsClick = {
                                focusManager.clearFocus()
                                onDetailsClick(it.first)
                            }
                        )
                        if (it !== exercises.last()) {
                            Spacer(modifier = Modifier.height(height = 1.dp))
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(suggestions.joinToString { it.first }) {
        isForcingSuggestionsCollapsing = false
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewTrainingPlanRegisterSwipeFormDark() {
    AppTheme {
        val currPage = remember { mutableStateOf(0) }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TrainingPlanRegisterSwipeForm(
                modifier = Modifier
                    .fillMaxHeight(),
                currPage = currPage.value,
                exercises = listOf(
                    Pair(
                        "1",
                        Triple(
                            Pair(
                                "Chest Exercise",
                                "Some notes about that."
                            ),
                            4,
                            12
                        )
                    )
                )
            )
        }
        LaunchedEffect(key1 = Unit) {
            repeat(100) {
                delay(2000)
                currPage.value = 1
                delay(2000)
                currPage.value = 2
                delay(2000)
                currPage.value = 0
                delay(2000)
                currPage.value = 2
                delay(2000)
                currPage.value = 1
                delay(5000)
            }
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewTrainingPlanRegisterSwipeFormLight() {
    AppTheme {
        val currPage = remember { mutableStateOf(0) }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TrainingPlanRegisterSwipeForm(
                modifier = Modifier
                    .fillMaxHeight(),
                currPage = currPage.value
            )
        }
        LaunchedEffect(key1 = Unit) {
            repeat(100) {
                delay(2000)
                currPage.value = 1
                delay(2000)
                currPage.value = 2
                delay(2000)
                currPage.value = 0
                delay(2000)
                currPage.value = 2
                delay(2000)
                currPage.value = 1
            }
        }
    }
}
