package com.ch4k4uw.workout.egym.training.plan.register

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.common.state.AppState
import com.ch4k4uw.workout.egym.common.ui.component.GenericTopAppBar
import com.ch4k4uw.workout.egym.core.extensions.asClickedState
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.core.ui.components.ContentLoadingProgressBar
import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlert
import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlertEffect
import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlertResultState
import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlertState
import com.ch4k4uw.workout.egym.core.ui.components.interaction.rememberModalBottomSheetAlert
import com.ch4k4uw.workout.egym.extensions.createSerializableStateListSaver
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanView
import com.ch4k4uw.workout.egym.training.plan.register.interaction.TrainingPlanExerciseView
import com.ch4k4uw.workout.egym.training.plan.register.interaction.TrainingPlanRegisterIntent
import com.ch4k4uw.workout.egym.training.plan.register.interaction.TrainingPlanRegisterState
import com.ch4k4uw.workout.egym.training.plan.register.interaction.TrainingPlanState
import com.ch4k4uw.workout.egym.training.plan.register.interaction.rememberSaveableTrainingPlanExerciseEditionState
import com.ch4k4uw.workout.egym.training.plan.register.interaction.rememberTrainingPlanState
import com.ch4k4uw.workout.egym.training.plan.register.ui.TrainingPlanExerciseEdition
import com.ch4k4uw.workout.egym.training.plan.register.ui.TrainingPlanRegisterSwipeForm
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun TrainingPlanRegisterScreen(
    events: Flow<AppState<TrainingPlanRegisterState>>,
    onIntent: (TrainingPlanRegisterIntent) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onDetailExercise: (String) -> Unit = {},
) {
    val coroutineScope = rememberCoroutineScope()

    val modalBottomSheetAlert = rememberModalBottomSheetAlert()

    var showTopProgress by rememberSaveable { mutableStateOf(false) }
    var showBlockProgress by rememberSaveable { mutableStateOf(false) }
    val modalExerciseEditionState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val trainingPlanSource = rememberSaveable { mutableStateOf(TrainingPlanView.Empty) }
    val trainingPlanState = rememberTrainingPlanState(source = trainingPlanSource)

    val selectedTrainingExercise = rememberSaveableTrainingPlanExerciseEditionState()
    val exercisesSuggestions = rememberSaveable(saver = createSerializableStateListSaver()) {
        mutableStateListOf<Pair<String, String>>()
    }

    var initialState by rememberSaveable { mutableStateOf("") }
    val askForSave by remember {
        derivedStateOf {
            initialState != calculateState(plan = trainingPlanSource.value)
        }
    }

    val focusManager = LocalFocusManager.current

    ModalBottomSheetLayout(
        sheetState = modalExerciseEditionState,
        sheetContent = {
            TrainingPlanExerciseEdition(
                notes = selectedTrainingExercise.notes.value,
                sets = selectedTrainingExercise.sets.value,
                reps = selectedTrainingExercise.reps.value,
                onConfirmClick = { notes, sets, reps ->
                    coroutineScope.launch {
                        modalExerciseEditionState.hide()
                    }
                    val index = trainingPlanSource.value.exercises
                        .indexOfFirst { it.exercise == selectedTrainingExercise.exercise.value }
                    if (index >= 0) {
                        with(trainingPlanSource.value) {
                            trainingPlanSource.value = copy(
                                exercises = exercises.toMutableList().apply {
                                    this[index] = this[index].copy(
                                        notes = notes,
                                        sets = sets,
                                        reps = reps
                                    )
                                }
                            )
                        }
                    }
                },
                onCloseClick = {
                    coroutineScope.launch {
                        modalExerciseEditionState.hide()
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            var currentFormPage by rememberSaveable { mutableStateOf(0) }
            val nextButtonEnableState = remember {
                derivedStateOf {
                    val isValidTitle = trainingPlanState.title.value.isNotBlank()
                    val isNotEndPage = currentFormPage < 2
                    val isValidExercises = trainingPlanState.exercises.value.isNotEmpty()
                    isValidTitle && (isNotEndPage || isValidExercises)
                }
            }
            TopBar(
                showTopProgress = showTopProgress,
                modalBottomSheetAlert = modalBottomSheetAlert,
                focusManager = focusManager,
                askForSave = askForSave,
                onPreviousPage = { (currentFormPage > 0).also { if (it) --currentFormPage } },
                onNavigateBack = onNavigateBack
            )
            //region swipe-form
            val surfaceColor = AppTheme.colors.material.primarySurface
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f),
                color = surfaceColor
            ) {
                TrainingPlanRegisterSwipeForm(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = AppTheme.Dimens.spacing.tiny,
                        start = AppTheme.Dimens.spacing.tiny,
                        end = AppTheme.Dimens.spacing.tiny,
                    ),
                    currPage = currentFormPage,
                    title = trainingPlanState.title.value,
                    description = trainingPlanState.description.value,
                    exercises = remember(trainingPlanState.exercises.value) {
                        derivedStateOf {
                            trainingPlanState.exercises.value.map { exerciseView ->
                                Pair(
                                    first = exerciseView.exercise,
                                    second = Triple(
                                        first = Pair(
                                            first = exerciseView.title,
                                            second = exerciseView.notes
                                        ),
                                        second = exerciseView.sets,
                                        third = exerciseView.reps,
                                    )
                                )
                            }
                        }
                    }.value,
                    exercisesSuggestions = exercisesSuggestions,
                    onTitleChange = {
                        trainingPlanSource.value = trainingPlanSource.value.copy(
                            title = it
                        )
                    },
                    onConfirmTitleAction = {
                        if (nextButtonEnableState.value) {
                            ++currentFormPage
                        }
                    },
                    onDescriptionChange = {
                        trainingPlanSource.value = trainingPlanSource.value.copy(
                            description = it
                        )
                    },
                    onAddExercise = {
                        with(trainingPlanSource.value) {
                            trainingPlanSource.value = copy(
                                exercises = exercises.toMutableList().apply {
                                    add(
                                        TrainingPlanExerciseView(
                                            exercise = it.first,
                                            title = it.second.first.first,
                                            notes = it.second.first.second,
                                            sets = it.second.second,
                                            reps = it.second.third
                                        )
                                    )
                                }
                            )
                        }
                    },
                    onEditExerciseClick = { id ->
                        trainingPlanSource.value.exercises
                            .find { it.exercise == id }
                            ?.also { exercise ->
                                selectedTrainingExercise.update(
                                    exercise = id,
                                    notes = exercise.notes,
                                    sets = exercise.sets,
                                    reps = exercise.reps
                                )
                            }
                        coroutineScope.launch {
                            modalExerciseEditionState.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    },
                    onDetailsExerciseClick = onDetailExercise,
                    onDeleteExerciseClick = { id ->
                        with(trainingPlanSource.value) {
                            trainingPlanSource.value = copy(
                                exercises = exercises.toMutableList().filter { it.exercise != id }
                            )
                        }
                    },
                    onExerciseQueryChange = {
                        onIntent(TrainingPlanRegisterIntent.PerformExerciseQuery(query = it))
                    }
                )
            }
            //endregion
            BottomActionButton(
                label = if (currentFormPage < 2) {
                    stringResource(id = R.string.training_plan_register_next_button_label)
                } else {
                    stringResource(id = R.string.training_plan_register_finish_button_label)
                },
                enabled = nextButtonEnableState.value,
                trainingPlanState = trainingPlanState,
                onClick = { (currentFormPage < 2).also { if (it) ++currentFormPage } },
                onIntent = onIntent
            )
        }
    }
    ContentLoadingProgressBar(visible = showBlockProgress)

    LaunchedEffect(key1 = Unit) {
        events.collect { event ->
            when (event) {
                is AppState.Loading -> {
                    when (event.tag) {
                        is TrainingPlanRegisterState.SearchTag -> showTopProgress = true
                        else -> showBlockProgress = true
                    }
                }
                is AppState.Loaded -> {
                    when (event.tag) {
                        is TrainingPlanRegisterState.SearchTag -> showTopProgress = false
                        else -> showBlockProgress = false
                    }
                }
                is AppState.Success -> {
                    when (event.content) {
                        is TrainingPlanRegisterState.ShowPlan -> {
                            initialState = calculateState(plan = event.content.plan)
                            trainingPlanSource.value = event.content.plan
                        }
                        is TrainingPlanRegisterState.ShowExerciseSuggestions -> {
                            exercisesSuggestions.clear()
                            exercisesSuggestions.addAll(
                                event.content.exercises.map { Pair(it.exercise, it.title) }
                            )
                        }
                        else -> Unit
                    }
                }
                else -> Unit
            }
        }
    }

    ModalBottomSheetAlertEffect(modalAlert = modalBottomSheetAlert) {
        asClickedState(R.id.training_plan_register_unsaved_action_confirmation) {
            hide()
            when (this) {
                is ModalBottomSheetAlertResultState.PositiveClicked -> {
                    onNavigateBack()
                }
                else -> Unit
            }
        }
    }
}

private fun calculateState(plan: TrainingPlanView): String {
    val title = "(${plan.title})"
    val description = "(${plan.description})"
    val exercises = "(${
        plan.exercises.joinToString(separator = "-") { exercise ->
            val exTitle = "(${exercise.title})"
            val exNotes = "(${exercise.notes})"
            val exSets = "(${exercise.sets})"
            val exReps = "(${exercise.reps})"
            "($exTitle-$exNotes-$exSets-$exReps)"
        }
    })"
    return "$title-$description-$exercises"
}

@Composable
private fun TopBar(
    showTopProgress: Boolean,
    modalBottomSheetAlert: ModalBottomSheetAlert,
    focusManager: FocusManager,
    askForSave: Boolean,
    onPreviousPage: () -> Boolean,
    onNavigateBack: () -> Unit,
) {
    val backNavigationConfirmationResource = object {
        val id = R.id.training_plan_register_unsaved_action_confirmation
        val title = stringResource(
            id = R.string.training_plan_register_back_action_confirmation_title
        )
        val message = stringResource(
            id = R.string.training_plan_register_back_action_confirmation_message
        )
        val positiveButton = stringResource(
            id = R.string.training_plan_register_back_action_confirmation_positive_button
        )
        val negativeButton = stringResource(
            id = R.string.training_plan_register_back_action_confirmation_negative_button
        )
    }
    fun onBackPress() {
        if (!onPreviousPage()) {
            if (askForSave) {
                focusManager.clearFocus()
                modalBottomSheetAlert
                    .showAlert(
                        callId = backNavigationConfirmationResource.id,
                        type = ModalBottomSheetAlertState.ModalType.Question,
                        title = backNavigationConfirmationResource.title,
                        message = backNavigationConfirmationResource.message,
                        positiveButtonLabel = backNavigationConfirmationResource
                            .positiveButton,
                        negativeButtonLabel = backNavigationConfirmationResource
                            .negativeButton
                    )
            } else {
                onNavigateBack()
            }
        }
    }
    GenericTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = stringResource(id = R.string.training_plan_register_title),
        actionIcons = {
            if (showTopProgress) {
                CircularProgressIndicator(
                    color = AppTheme.colors.material.secondary
                )
            }
        },
        onNavigateBack = {
            onBackPress()
        }
    )
    BackHandler {
        onBackPress()
    }
}

@Composable
private fun BottomActionButton(
    label: String,
    enabled: Boolean,
    trainingPlanState: TrainingPlanState,
    onClick: () -> Boolean,
    onIntent: (TrainingPlanRegisterIntent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = AppTheme.Dimens.spacing.tiny)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(percent = 50),
            enabled = enabled,
            onClick = {
                if (!onClick()) {
                    onIntent(
                        TrainingPlanRegisterIntent.SavePlan(
                            plan = TrainingPlanView(
                                id = trainingPlanState.id.value,
                                title = trainingPlanState.title.value,
                                description = trainingPlanState.description.value,
                                exercises = trainingPlanState.exercises.value
                            )
                        )
                    )
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = AppTheme.colors.material.secondary
            )
        ) {
            Text(text = label)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewTrainingPlanRegisterScreenDark() {

}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PreviewTrainingPlanRegisterScreenLight() {

}