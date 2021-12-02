package com.ch4k4uw.workout.egym.exercise

import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.constraintlayout.compose.ConstraintLayout
import com.ch4k4uw.workout.egym.common.ui.component.ProfileDialog
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.core.ui.components.ListLoadingShimmer1
import com.ch4k4uw.workout.egym.core.ui.components.ShimmerCardListItem2
import com.ch4k4uw.workout.egym.exercise.interaction.ExerciseHeadView
import com.ch4k4uw.workout.egym.exercise.interaction.ExerciseListIntent
import com.ch4k4uw.workout.egym.exercise.interaction.ExerciseListState
import com.ch4k4uw.workout.egym.exercise.ui.component.ExerciseListHeadCard
import com.ch4k4uw.workout.egym.exercise.ui.component.ExerciseListTopAppBar
import com.ch4k4uw.workout.egym.exercise.ui.component.ExerciseListTopSearchBar
import com.ch4k4uw.workout.egym.extensions.asLoading
import com.ch4k4uw.workout.egym.extensions.handleError
import com.ch4k4uw.workout.egym.extensions.handleSuccess
import com.ch4k4uw.workout.egym.extensions.isLoading
import com.ch4k4uw.workout.egym.extensions.raiseEvent
import com.ch4k4uw.workout.egym.login.interaction.UserView
import com.ch4k4uw.workout.egym.state.AppState

private object SearchBarAnimationConstants {
    private const val MinXScale = .9f
    private const val MinYScale = .5f
    private const val MinCornerSize = 10f

    fun calculateXScale(scale: Float) = (1f - MinXScale) - ((1f - MinXScale) * scale)
    fun calculateYScale(scale: Float) = (1f - MinYScale) - ((1f - MinYScale) * scale)
    fun calculateCornerSize(scale: Float) = MinCornerSize - (MinCornerSize * scale)
}

@ExperimentalComposeUiApi
@ExperimentalUnitApi
@Composable
fun ExerciseListScreen(
    uiState: State<AppState<ExerciseListState>>,
    onIntent: (ExerciseListIntent) -> Unit = {},
    onLoggedOut: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onNavigationStateChanged: (enable: Boolean) -> Unit = {}
) {
    var userData by rememberSaveable { mutableStateOf(UserView.Empty) }
    var isProfileDialogShowing by rememberSaveable { mutableStateOf(false) }
    val exercisesHeads = rememberSaveable(saver = exerciseHeadListSaver()) {
        mutableStateListOf()
    }
    var showShimmer by rememberSaveable { mutableStateOf(true) }
    var queryText by rememberSaveable { mutableStateOf("") }
    var isLoadingStateForced by rememberSaveable { mutableStateOf(false) }
    var isResetQueryRequired by rememberSaveable { mutableStateOf(false) }

    if (uiState.isLoading) {
        isLoadingStateForced = false
    }

    uiState.raiseEvent().apply {
        if (this !is AppState.Idle<*>) {
            onNavigationStateChanged(this !is AppState.Loading<*>)
        }
        handleSuccess {
            when (content) {
                is ExerciseListState.DisplayUserData -> userData = content.user
                is ExerciseListState.ShowLoginScreen -> onLoggedOut()
                is ExerciseListState.ShowExerciseList -> with(exercisesHeads) {
                    clear()
                    addAll(content.exercises)
                }
                is ExerciseListState.ShowNoMorePagesToFetch -> showShimmer = false
                else -> Unit
            }
        }
        handleError {
            showShimmer = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                ConstraintLayout {
                    var searchBarAnimTarget by rememberSaveable { mutableStateOf(0f) }
                    val searchBarAnim by animateFloatAsState(
                        targetValue = searchBarAnimTarget,
                        animationSpec = tween(durationMillis = 300)
                    )
                    val (topBar, searchBar) = createRefs()

                    ExerciseListTopAppBar(
                        modifier = Modifier
                            .constrainAs(topBar) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        queryText = queryText,
                        profileImage = userData.image,
                        onNavigateBack = onNavigateBack,
                        onSearchButtonClick = { searchBarAnimTarget = 1f },
                        onProfileButtonClick = { isProfileDialogShowing = true }
                    )

                    if (searchBarAnim > 0f) {
                        val xAnimFactor = SearchBarAnimationConstants.calculateXScale(searchBarAnim)
                        val yAnimFactor = SearchBarAnimationConstants.calculateYScale(searchBarAnim)
                        val animCornerFactor = SearchBarAnimationConstants
                            .calculateCornerSize(searchBarAnim)
                        ExerciseListTopSearchBar(
                            query = queryText,
                            modifier = Modifier
                                .constrainAs(searchBar) {
                                    top.linkTo(topBar.top)
                                    start.linkTo(topBar.start)
                                    bottom.linkTo(topBar.bottom)
                                    end.linkTo(topBar.end)
                                }
                                .alpha(alpha = searchBarAnim)
                                .scale(scaleX = 1 - xAnimFactor, scaleY = 1f - yAnimFactor)
                                .clip(shape = RoundedCornerShape(size = animCornerFactor)),
                            onNavigationClick = {
                                if (isResetQueryRequired) {
                                    showShimmer = true
                                    exercisesHeads.clear()
                                    isLoadingStateForced = true
                                    isResetQueryRequired = false
                                    onIntent(ExerciseListIntent.PerformQuery(query = queryText))
                                }
                                searchBarAnimTarget = 0f
                            },
                            onQueryChanged = {
                                showShimmer = true
                                exercisesHeads.clear()
                                isLoadingStateForced = true
                                isResetQueryRequired = it != queryText
                                onIntent(ExerciseListIntent.PerformQuery(query = it))
                            },
                            onExecuteSearch = {
                                queryText = it
                                searchBarAnimTarget = 0f
                            }
                        )
                    }
                }
            },
            content = {
                val uiStateValue = uiState.value
                if (isLoadingStateForced || uiStateValue is AppState.Loading<*>) {
                    if (
                        isLoadingStateForced ||
                        uiStateValue.asLoading()?.tag is ExerciseListState.ExerciseListTag
                    ) {
                        if (exercisesHeads.isEmpty()) {
                            ListLoadingShimmer1()
                        }
                    }
                }
                if (exercisesHeads.isNotEmpty()) {
                    LazyColumn {
                        val exercisesCount = exercisesHeads.size
                        items(count = exercisesCount, key = { exercisesHeads[it].id }) { index ->
                            ExerciseListHeadCard(
                                imageUrl = exercisesHeads[index].image,
                                title = exercisesHeads[index].title
                            )
                        }
                        if (showShimmer) {
                            item {
                                ShimmerCardListItem2 {
                                    onIntent(ExerciseListIntent.FetchNextPage)
                                }
                            }
                        }

                    }
                }
            }
        )
        if (isProfileDialogShowing) {
            ProfileDialog(
                image = userData.image,
                name = userData.name,
                email = userData.email,
                onDismissRequest = { isProfileDialogShowing = false },
                onLogout = { onIntent(ExerciseListIntent.PerformLogout) }
            )
        }
    }
}

fun exerciseHeadListSaver(): Saver<SnapshotStateList<ExerciseHeadView>, *> =
    Saver(
        save = {
            Bundle().apply {
                putParcelableArray("exs", it.toTypedArray())
            }
        },
        restore = {
            (it.getParcelableArray("exs") as? Array<*>)
                ?.map { item -> item as ExerciseHeadView }
                ?.let { items ->
                    mutableStateListOf<ExerciseHeadView>().apply {
                        addAll(items)
                    }
                }
        }
    )

@ExperimentalComposeUiApi
@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDarkScreen() {
    AppTheme {
        ExerciseListScreen(
            uiState = remember { mutableStateOf(AppState.Loading()) }
        )
    }
}

@ExperimentalComposeUiApi
@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewLightScreen() {
    AppTheme {
        ExerciseListScreen(
            uiState = remember { mutableStateOf(AppState.Loading()) }
        )
    }
}