package com.ch4k4uw.workout.egym.exercise.list

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.common.ui.component.ProfileDialog
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseHeadView
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseListIntent
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseListState
import com.ch4k4uw.workout.egym.exercise.list.ui.component.ExerciseListListSlot
import com.ch4k4uw.workout.egym.exercise.list.ui.component.ExerciseListTopAppBarSlot
import com.ch4k4uw.workout.egym.exercise.list.ui.component.ExerciseListTopTagChipBarSlot
import com.ch4k4uw.workout.egym.extensions.handleError
import com.ch4k4uw.workout.egym.extensions.handleSuccess
import com.ch4k4uw.workout.egym.extensions.isLoading
import com.ch4k4uw.workout.egym.extensions.raiseEvent
import com.ch4k4uw.workout.egym.login.interaction.UserView
import com.ch4k4uw.workout.egym.common.state.AppState
import kotlin.math.roundToInt

private enum class LayoutId {
    TopBar, Tags, List
}

@ExperimentalComposeUiApi
@ExperimentalUnitApi
@Composable
fun ExerciseListScreen(
    uiState: State<AppState<ExerciseListState>>,
    onIntent: (ExerciseListIntent) -> Unit = {},
    onLoggedOut: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onExerciseClick: (String) -> Unit = {}
) {
    var topBarHeightPx by rememberSaveable { mutableStateOf(-1) }
    val toolbarOffsetHeightPx = rememberSaveable { mutableStateOf(0f) }
    var tagsHeightPx by rememberSaveable { mutableStateOf(-1) }
    val tagsOffsetHeightPx = rememberSaveable { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            val isConsumedByTags: Boolean
                get() = tagsOffsetHeightPx.value == 0f ||
                        tagsOffsetHeightPx.value == -(topBarHeightPx + tagsHeightPx).toFloat()

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y

                val newTagsOffset = tagsOffsetHeightPx.value + delta
                tagsOffsetHeightPx.value = newTagsOffset
                    .coerceIn(-(topBarHeightPx + tagsHeightPx).toFloat(), 0f)

                val newToolbarOffset = tagsOffsetHeightPx.value + tagsHeightPx.toFloat() + delta
                toolbarOffsetHeightPx.value = newToolbarOffset
                    .coerceIn(-topBarHeightPx.toFloat(), 0f)

                return if (isConsumedByTags) {
                    Offset.Zero
                } else {
                    available
                }
            }
        }
    }

    val userData = rememberSaveable { mutableStateOf(UserView.Empty) }
    val isProfileDialogShowing = rememberSaveable { mutableStateOf(false) }
    val exercisesHeads = rememberSaveable(saver = exerciseHeadListSaver) { mutableStateListOf() }
    val exerciseTags = rememberSaveable(saver = exerciseTagListSaver) { mutableStateListOf() }
    val showShimmer = rememberSaveable { mutableStateOf(true) }
    val queryText = rememberSaveable { mutableStateOf("") }
    val isLoadingStateForced = rememberSaveable { mutableStateOf(false) }
    val isResetQueryRequired = rememberSaveable { mutableStateOf(false) }

    if (uiState.isLoading) {
        isLoadingStateForced.value = false
    }

    uiState.raiseEvent().apply {
        handleSuccess {
            when (content) {
                is ExerciseListState.DisplayUserData -> userData.value = content.user
                is ExerciseListState.ShowLoginScreen -> onLoggedOut()
                is ExerciseListState.ShowExerciseList -> with(exercisesHeads) {
                    clear()
                    addAll(content.exercises)
                }
                is ExerciseListState.ShowNoMorePagesToFetch -> showShimmer.value = false
                is ExerciseListState.ShowExerciseTagList -> with(exerciseTags) {
                    clear()
                    addAll(content.tags.map { Pair(it, false) })
                }
                else -> Unit
            }
        }
        handleError {
            showShimmer.value = false
        }
    }

    Layout(
        modifier = Modifier
            .nestedScroll(connection = nestedScrollConnection),
        content = {
            ExerciseListTopAppBarSlot(
                modifier = Modifier.layoutId(layoutId = LayoutId.TopBar),
                queryText = queryText,
                exerciseTags = exerciseTags.filter { it.second }.map { it.first },
                userData = userData,
                onNavigateBack = onNavigateBack,
                isProfileDialogShowing = isProfileDialogShowing,
                isResetQueryRequired = isResetQueryRequired,
                showShimmer = showShimmer,
                exercisesHeads = exercisesHeads,
                isLoadingStateForced = isLoadingStateForced,
                onIntent = onIntent,
            )
            ExerciseListTopTagChipBarSlot(
                modifier = Modifier
                    .layoutId(layoutId = LayoutId.Tags),
                queryText = queryText,
                exerciseTags = exerciseTags,
                showShimmer = showShimmer,
                exercisesHeads = exercisesHeads,
                isLoadingStateForced = isLoadingStateForced,
                onIntent = onIntent,
            )
            ExerciseListListSlot(
                modifier = Modifier.layoutId(layoutId = LayoutId.List),
                uiState = uiState,
                isLoadingStateForced = isLoadingStateForced,
                exercisesHeads = exercisesHeads,
                showShimmer = showShimmer,
                onExerciseClick = onExerciseClick,
                onIntent = onIntent,
            )
        }
    ) { measure, constraints ->
        val indexes = hashMapOf<LayoutId, Int>().apply {
            measure.forEachIndexed { index, measurable ->
                val id = measurable.layoutId
                if (id is LayoutId) this[id] = index
            }
        }

        val placeable = measure.map { it.measure(constraints = constraints) }
        val wMax = placeable.maxOf { it.width }
        val hMax = placeable.maxOf { it.height }

        indexes[LayoutId.TopBar]?.run(placeable::get)?.apply {
            topBarHeightPx = height
        }
        indexes[LayoutId.Tags]?.run(placeable::get)?.apply {
            tagsHeightPx = height
        }

        layout(width = wMax, height = hMax) {
            val yTopBar = toolbarOffsetHeightPx.value.roundToInt()
            val yTags = tagsOffsetHeightPx.value.roundToInt()
            val placeStack = Array<(() -> Unit)?>(3) { null }

            indexes[LayoutId.TopBar]?.also { topBarIndex ->
                placeStack[2] = { placeable[topBarIndex].place(x = 0, y = yTopBar) }
                indexes[LayoutId.Tags]?.also { tagsIndex ->
                    placeStack[1] = {
                        placeable[tagsIndex].place(
                            x = 0,
                            y = placeable[topBarIndex].height + yTopBar + yTags
                        )
                    }
                    indexes[LayoutId.List]?.also { listIndex ->
                        placeStack[0] = {
                            placeable[listIndex].place(
                                x = 0,
                                y = placeable[topBarIndex].height + placeable[tagsIndex].height + yTags
                            )
                        }
                    }
                }
            }

            placeStack.forEach { it?.invoke() }
        }
    }

    if (isProfileDialogShowing.value) {
        ProfileDialog(
            image = userData.value.image,
            name = userData.value.name,
            email = userData.value.email,
            onDismissRequest = { isProfileDialogShowing.value = false },
            onLogout = { onIntent(ExerciseListIntent.PerformLogout) }
        )
    }
}

private val exerciseHeadListSaver: Saver<SnapshotStateList<ExerciseHeadView>, *> =
    Saver(
        save = {
            Bundle().apply { putParcelableArray("exs", it.toTypedArray()) }
        },
        restore = {
            (it.getParcelableArray("exs") as? Array<*>)
                ?.map { item -> item as ExerciseHeadView }
                ?.let { items -> mutableStateListOf<ExerciseHeadView>().apply { addAll(items) } }
        }
    )

private val exerciseTagListSaver: Saver<SnapshotStateList<Pair<ExerciseTag, Boolean>>, *> =
    Saver(
        save = {
            Bundle().apply { putSerializable("tags", it.toTypedArray()) }
        },
        restore = {
            (it.getSerializable("tags") as? Array<*>)
                ?.let { tags -> tags.map { tag -> tag as Pair<*, *> } }
                ?.let { tags ->
                    tags.map { tag -> Pair(tag.first as ExerciseTag, tag.second as Boolean) }
                }
                ?.let { tags ->
                    mutableStateListOf<Pair<ExerciseTag, Boolean>>().apply { addAll(tags) }
                }
        }
    )