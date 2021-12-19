package com.ch4k4uw.workout.egym.training.plan.register.ui

import android.content.res.Configuration
import android.os.Bundle
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.ch4k4uw.workout.egym.R
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

    @Suppress("unchecked_cast")
    val textFieldValueSaver: Saver<MutableState<TextFieldValue>, *> = Saver(
        save = {
            Bundle().apply {
                putString("text", it.value.text)
                putSerializable("sel", Pair(it.value.selection.start, it.value.selection.end))
            }
        },
        restore = {
            val text = it.getString("text").orEmpty()
            val selection = it.getSerializable("sel") as? Pair<Int, Int>
                ?: Pair(text.length, text.length)
            mutableStateOf(
                TextFieldValue(
                    text = text,
                    selection = TextRange(start = selection.first, end = selection.second)
                )
            )
        }
    )

    const val MAX_DESCRIPTION_LENGTH = 1024
    const val DEFAULT_SETS = 3
    const val DEFAULT_REPS = 12
}

@Composable
fun TrainingPlanRegisterSwipeForm(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    currPage: Int,
    title: String = "",
    description: String = "",
    exercises: List<Pair<String, Triple<Pair<String, String>, Int, Int>>> = listOf(),
    exercisesSuggestions: List<Pair<String, String>> = listOf(),
    onTitleChange: (String) -> Unit = {},
    onConfirmTitleAction: () -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onAddExercise: (Pair<String, Triple<Pair<String, String>, Int, Int>>) -> Unit = {},
    onEditExerciseClick: (String) -> Unit = {},
    onDetailsExerciseClick: (String) -> Unit = {},
    onDeleteExerciseClick: (String) -> Unit = {},
    onExerciseQueryChange: (String) -> Unit = {},
) {
    var pageSize by rememberSaveable { mutableStateOf(0) }
    val transitionState = remember {
        MutableTransitionState(0)
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
                                    onDone = onConfirmTitleAction
                                )
                            }
                            TrainingPlanRegisterSwipeForm.LayoutId.Second -> {
                                PlanDescriptionForm(
                                    description = description,
                                    focusRequester = planDescriptionFocusRequester,
                                    onTextChange = onDescriptionChange
                                )
                            }
                            TrainingPlanRegisterSwipeForm.LayoutId.Third -> {
                                PlanExercisesForm(
                                    exercises = exercises,
                                    suggestions = exercisesSuggestions,
                                    onSearchChange = onExerciseQueryChange,
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
    title: String,
    focusRequester: FocusRequester,
    onTextChange: (String) -> Unit,
    onDone: () -> Unit,
) {
    var titleFieldValue by rememberSaveable(
        saver = TrainingPlanRegisterSwipeForm.textFieldValueSaver
    ) {
        mutableStateOf(
            TextFieldValue(
                text = title,
                selection = TextRange(start = 0, end = title.length)
            )
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { state ->
                    if (state.isFocused) {
                        titleFieldValue = titleFieldValue.copy(
                            text = titleFieldValue.text,
                            selection = TextRange(start = 0, end = title.length)
                        )
                    }
                }
                .focusRequester(focusRequester = focusRequester),
            value = titleFieldValue,
            label = {
                Text(
                    text = stringResource(id = R.string.training_plan_register_form_plan_title_label),
                    style = AppTheme.typography.material.body1
                )
            },
            onValueChange = {
                if (title != it.text) {
                    onTextChange(it.text)
                }
                titleFieldValue = it
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
    description: String,
    focusRequester: FocusRequester,
    onTextChange: (String) -> Unit
) {
    var titleFieldValue by rememberSaveable(
        saver = TrainingPlanRegisterSwipeForm.textFieldValueSaver
    ) {
        mutableStateOf(
            TextFieldValue(
                text = description,
                selection = TextRange(start = 0, end = description.length)
            )
        )
    }

    val height = with(LocalDensity.current) { (LocalTextStyle.current.fontSize * 10).toDp() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = height)
                .onFocusChanged { state ->
                    if (state.isFocused) {
                        titleFieldValue = titleFieldValue.copy(
                            text = titleFieldValue.text,
                            selection = TextRange(start = 0, end = description.length)
                        )
                    }
                }
                .focusRequester(focusRequester = focusRequester),
            value = titleFieldValue,
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
                    if (description != it.text) {
                        onTextChange(it.text)
                    }
                    titleFieldValue = it
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
    onSearchChange: (String) -> Unit,
    onAddClick: (Pair<String, String>) -> Unit,
    onEditClick: (String) -> Unit,
    onDetailsClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
) {
    var isSuggestionExpanded by remember { mutableStateOf(false) }
    var selectedSuggestion by rememberSaveable { mutableStateOf(Pair("", "")) }

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
                    isSuggestionExpanded = !isSuggestionExpanded && suggestions.isNotEmpty()
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
                    onDismissRequest = { isSuggestionExpanded = false }
                ) {
                    suggestions.forEach { suggestion ->
                        DropdownMenuItem(
                            onClick = {
                                selectedSuggestion = suggestion
                                isSuggestionExpanded = false
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier
                                        .weight(weight = 1f),
                                    text = suggestion.first
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
            val addEnableState by remember {
                derivedStateOf {
                    selectedSuggestion.first.isNotBlank() && !suggestions.any {
                        it.first == selectedSuggestion.first
                    }
                }
            }
            IconButton(
                enabled = addEnableState,
                onClick = {
                    onAddClick(selectedSuggestion)
                    selectedSuggestion = Pair("", "")
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
        Spacer(modifier = Modifier.height(AppTheme.Dimens.spacing.xtiny))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1f)
        ) {
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
                        onEditClick = {
                            onEditClick(it.first)
                        },
                        onDeleteClick = {
                            onDeleteClick(it.first)
                        },
                        onDetailsClick = {
                            onDetailsClick(it.first)
                        }
                    )
                }
            }
        }
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
