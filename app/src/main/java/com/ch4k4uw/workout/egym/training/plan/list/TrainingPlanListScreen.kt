package com.ch4k4uw.workout.egym.training.plan.list

import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.common.state.AppState
import com.ch4k4uw.workout.egym.common.ui.component.EmptyContentPlaceholder
import com.ch4k4uw.workout.egym.common.ui.component.GenericTopAppBar
import com.ch4k4uw.workout.egym.core.common.domain.data.NoConnectivityException
import com.ch4k4uw.workout.egym.core.extensions.asClickedState
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.core.ui.components.ContentLoadingProgressBar
import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlertEffect
import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlertResultState
import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlertState
import com.ch4k4uw.workout.egym.core.ui.components.interaction.rememberModalBottomSheetAlert
import com.ch4k4uw.workout.egym.login.interaction.UserView
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanListIntent
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanListState
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanView
import com.ch4k4uw.workout.egym.training.plan.list.interaction.rememberTrainingPlanDeletionResources
import com.ch4k4uw.workout.egym.training.plan.list.ui.component.TrainingPlanCard
import com.ch4k4uw.workout.egym.training.plan.list.ui.component.rememberSaveableTopBarStateHolder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.math.roundToInt

private enum class LayoutId {
    TopBar, Content, Fab
}

@Composable
fun TrainingPlanListScreen(
    uiState: Flow<AppState<TrainingPlanListState>>,
    onIntent: (TrainingPlanListIntent) -> Unit = {},
    onLoggedOut: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onCreatePlan: () -> Unit = {}
) {
    val modalBottomSheetAlert = rememberModalBottomSheetAlert()

    val userData = rememberSaveable { mutableStateOf(UserView.Empty) }
    val planToDelete = rememberSaveable { mutableStateOf(TrainingPlanView.Empty) }
    val trainingList = rememberSaveable(saver = trainingPlanListSaver) { mutableStateListOf() }
    val isShowEmptyListMessage = rememberSaveable { mutableStateOf(true) }
    val isShowRefreshButton = rememberSaveable { mutableStateOf(false) }
    val planDeletionResources = rememberTrainingPlanDeletionResources()
    val topBarStateHolder = rememberSaveableTopBarStateHolder()

    val fabPadding = AppTheme.Dimens.spacing.normal

    Layout(
        modifier = Modifier
            .nestedScroll(connection = topBarStateHolder.nestedScrollConnection),
        content = {
            GenericTopAppBar(
                modifier = Modifier
                    .layoutId(LayoutId.TopBar),
                title = stringResource(id = R.string.training_plan_list_title),
                userData = userData.value,
                onNavigateBack = onNavigateBack,
                onLogoutClick = { onIntent(TrainingPlanListIntent.PerformLogout) }
            )
            Box(
                modifier = Modifier
                    .layoutId(LayoutId.Content)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    if (isShowEmptyListMessage.value) {
                        item {
                            EmptyContentPlaceholder(
                                onRefreshClick = if (isShowRefreshButton.value) {
                                    {
                                        onIntent(TrainingPlanListIntent.FetchPlanList)
                                    }
                                } else {
                                    null
                                }
                            )
                        }
                    } else {
                        val exercisesCount = trainingList.size
                        items(count = exercisesCount, key = { trainingList[it].id }) { index ->
                            Column {
                                TrainingPlanCard(
                                    title = trainingList[index].title,
                                    description = trainingList[index].description,
                                    onClick = { },
                                    onEditClick = { },
                                    onDeleteClick = {
                                        planToDelete.value = trainingList[index]
                                        onIntent(
                                            TrainingPlanListIntent.DeletePlan(
                                                plan = trainingList[index]
                                            )
                                        )
                                    }
                                )
                                if (index < trainingList.size - 1) {
                                    Spacer(modifier = Modifier.height(height = 1.dp))
                                }
                            }
                        }
                    }
                }
            }

            FloatingActionButton(
                modifier = Modifier
                    .layoutId(LayoutId.Fab)
                    .rotate(degrees = 360f * topBarStateHolder.topBarTransition),
                onClick = onCreatePlan
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }

        }
    ) { measurable, constraints ->
        val placeableMapping = LayoutId.values().map { layoutId ->
            measurable.find { it.layoutId == layoutId }?.measure(constraints = constraints)
        }

        val wMax = placeableMapping.maxOf { it?.width ?: 0 }
        val hMax = placeableMapping.maxOf { it?.height ?: 0 }

        topBarStateHolder
            .update(topBarHeight = placeableMapping[LayoutId.TopBar.ordinal]?.height ?: 0)

        layout(width = wMax, height = hMax) {
            placeableMapping.mapIndexed { index, placeable ->
                when (index) {
                    LayoutId.Fab.ordinal -> {
                        placeable?.let {
                            {
                                val baseY = hMax - it.height - fabPadding.toPx().roundToInt()
                                val delta = hMax - baseY
                                val transition = topBarStateHolder.topBarTransition
                                it.place(
                                    x = wMax - it.width - fabPadding.toPx().roundToInt(),
                                    y = (baseY + delta * transition).roundToInt()
                                )
                            }
                        }
                    }
                    LayoutId.TopBar.ordinal -> {
                        placeable?.let {
                            {
                                it.place(
                                    x = 0,
                                    y = topBarStateHolder.topBarOffset.roundToInt()
                                )
                            }
                        }
                    }
                    LayoutId.Content.ordinal -> {
                        placeable?.let {
                            {
                                val baseY = placeableMapping[LayoutId.TopBar.ordinal]?.height ?: 0
                                it.place(
                                    x = 0,
                                    y = baseY + topBarStateHolder.topBarOffset.roundToInt()
                                )
                            }
                        }
                    }
                    else -> null
                }
            }.forEach { it?.invoke() }
        }
    }

    var isShowingLoading by rememberSaveable { mutableStateOf(false) }
    ContentLoadingProgressBar(visible = isShowingLoading)

    LaunchedEffect(Unit) {
        uiState.collect { state ->
            when (state) {
                is AppState.Loading -> state.apply {
                    isShowingLoading = true
                }
                is AppState.Loaded -> state.apply {
                    isShowingLoading = false
                }
                is AppState.Success -> state.apply {
                    when (content) {
                        is TrainingPlanListState.DisplayUserData -> userData.value = content.user
                        is TrainingPlanListState.ShowPlanList -> {
                            isShowEmptyListMessage.value = false
                            trainingList.clear()
                            trainingList.addAll(content.plans)
                        }
                        is TrainingPlanListState.DisplayNoPlansToShowMessage -> {
                            isShowEmptyListMessage.value = true
                            isShowRefreshButton.value = true
                        }
                        is TrainingPlanListState.ConfirmPlanDeletion ->
                            modalBottomSheetAlert
                                .showAlert(
                                    callId = planDeletionResources.id,
                                    type = ModalBottomSheetAlertState.ModalType.Question,
                                    title = planDeletionResources.title,
                                    message = planDeletionResources.message(content.plan.title),
                                    positiveButtonLabel = planDeletionResources.positiveButtonLabel,
                                    negativeButtonLabel = planDeletionResources.negativeButtonLabel
                                )
                        is TrainingPlanListState.ShowLoginScreen -> onLoggedOut()
                        else -> Unit
                    }
                }
                is AppState.Error -> state.apply {
                    when (tag) {
                        is TrainingPlanListState.FetchUserDataTag,
                        is TrainingPlanListState.FetchPlanListTag,
                        is TrainingPlanListState.PerformLogoutTag,
                        is TrainingPlanListState.DeletePlanTag -> {
                            when (tag) {
                                is TrainingPlanListState.FetchUserDataTag ->
                                    R.id.training_plan_list_fetch_user_error
                                is TrainingPlanListState.FetchPlanListTag ->
                                    R.id.training_plan_list_error
                                is TrainingPlanListState.PerformLogoutTag ->
                                    R.id.training_plan_list_logout_error
                                is TrainingPlanListState.DeletePlanTag ->
                                    R.id.training_plan_list_delete_plan_error
                                else -> 0
                            }.takeIf { it != 0 }?.also { callId ->
                                when (cause) {
                                    is NoConnectivityException -> modalBottomSheetAlert
                                        .showConnectivityErrorAlert(
                                            callId = callId
                                        )
                                    else -> modalBottomSheetAlert
                                        .showGenericErrorAlert(
                                            callId = callId
                                        )
                                }
                            }
                        }
                        else -> Unit
                    }
                }
                else -> Unit
            }
        }
    }

    ModalBottomSheetAlertEffect(modalAlert = modalBottomSheetAlert) {
        asClickedState(
            R.id.training_plan_list_fetch_user_error,
            R.id.training_plan_list_error,
            R.id.training_plan_list_logout_error,
            R.id.training_plan_list_delete_plan_error,
        ) {
            hide()
            when (this) {
                is ModalBottomSheetAlertResultState.PositiveClicked -> {
                    when (callId) {
                        R.id.training_plan_list_fetch_user_error ->
                            TrainingPlanListIntent.FetchUserData
                        R.id.training_plan_list_error ->
                            TrainingPlanListIntent.FetchPlanList
                        R.id.training_plan_list_logout_error ->
                            TrainingPlanListIntent.PerformLogout
                        R.id.training_plan_list_delete_plan_error ->
                            TrainingPlanListIntent.DeletePlan(
                                plan = planToDelete.value,
                                confirmed = true
                            )
                        else -> null
                    }?.also(onIntent)
                }
                else -> {
                    isShowEmptyListMessage.value = true
                    isShowRefreshButton.value = callId == R.id.training_plan_list_error
                }
            }
        }
        asClickedState(planDeletionResources.id) {
            hide()
            when (this) {
                is ModalBottomSheetAlertResultState.PositiveClicked -> {
                    onIntent(
                        TrainingPlanListIntent.DeletePlan(
                            plan = planToDelete.value,
                            confirmed = true
                        )
                    )
                }
                else -> Unit
            }
        }
    }
}

private val trainingPlanListSaver: Saver<SnapshotStateList<TrainingPlanView>, *> =
    Saver(
        save = {
            Bundle().apply { putParcelableArray("tps", it.toTypedArray()) }
        },
        restore = {
            (it.getParcelableArray("tps") as? Array<*>)
                ?.map { item -> item as TrainingPlanView }
                ?.let { items -> mutableStateListOf<TrainingPlanView>().apply { addAll(items) } }
        }
    )