package com.ch4k4uw.workout.egym.exercise.list.ui.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.constraintlayout.compose.ConstraintLayout
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseHeadView
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseListIntent
import com.ch4k4uw.workout.egym.login.interaction.UserView

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
fun ExerciseListTopAppBarSlot(
    modifier: Modifier = Modifier,
    queryText: MutableState<String>,
    exerciseTags: List<ExerciseTag>,
    userData: State<UserView>,
    onNavigateBack: () -> Unit,
    isProfileDialogShowing: MutableState<Boolean>,
    isResetQueryRequired: MutableState<Boolean>,
    showShimmer: MutableState<Boolean>,
    exercisesHeads: MutableList<ExerciseHeadView>,
    isLoadingStateForced: MutableState<Boolean>,
    onIntent: (ExerciseListIntent) -> Unit
) {
    ConstraintLayout(modifier = modifier) {
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
            queryText = queryText.value,
            profileImage = userData.value.image,
            onNavigateBack = onNavigateBack,
            onSearchButtonClick = { searchBarAnimTarget = 1f },
            onProfileButtonClick = { isProfileDialogShowing.value = true }
        )

        if (searchBarAnim > 0f) {
            val xAnimFactor = SearchBarAnimationConstants.calculateXScale(searchBarAnim)
            val yAnimFactor = SearchBarAnimationConstants.calculateYScale(searchBarAnim)
            val animCornerFactor = SearchBarAnimationConstants
                .calculateCornerSize(searchBarAnim)
            ExerciseListTopSearchBar(
                query = queryText.value,
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
                    if (isResetQueryRequired.value) {
                        showShimmer.value = true
                        exercisesHeads.clear()
                        isLoadingStateForced.value = true
                        isResetQueryRequired.value = false
                        onIntent(
                            ExerciseListIntent.PerformQuery(
                                query = queryText.value,
                                tags = exerciseTags
                            )
                        )
                    }
                    searchBarAnimTarget = 0f
                },
                onQueryChanged = {
                    showShimmer.value = true
                    exercisesHeads.clear()
                    isLoadingStateForced.value = true
                    isResetQueryRequired.value = it != queryText.value
                    onIntent(ExerciseListIntent.PerformQuery(query = it, tags = exerciseTags))
                },
                onExecuteSearch = {
                    queryText.value = it
                    searchBarAnimTarget = 0f
                }
            )
        }
    }
}