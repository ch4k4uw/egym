package com.ch4k4uw.workout.egym.exercise.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.common.ui.theme.EGymTheme
import com.ch4k4uw.workout.egym.core.ui.components.ListLoadingShimmer1
import com.ch4k4uw.workout.egym.core.ui.components.ShimmerCardListItem2
import com.ch4k4uw.workout.egym.exercise.interaction.ExerciseHeadView
import com.ch4k4uw.workout.egym.exercise.interaction.ExerciseListIntent
import com.ch4k4uw.workout.egym.exercise.interaction.ExerciseListState
import com.ch4k4uw.workout.egym.extensions.asLoading
import com.ch4k4uw.workout.egym.state.AppState

@ExperimentalUnitApi
@Composable
fun ExerciseListListSlot(
    modifier: Modifier,
    uiState: State<AppState<ExerciseListState>>,
    isLoadingStateForced: MutableState<Boolean>,
    exercisesHeads: SnapshotStateList<ExerciseHeadView>,
    showShimmer: MutableState<Boolean>,
    onIntent: (ExerciseListIntent) -> Unit
) {
    Box(modifier = modifier) {
        val uiStateValue = uiState.value
        if (isLoadingStateForced.value || uiStateValue is AppState.Loading<*>) {
            if (
                isLoadingStateForced.value ||
                uiStateValue.asLoading()?.tag is ExerciseListState.ExerciseListTag
            ) {
                if (exercisesHeads.isEmpty()) {
                    ListLoadingShimmer1(
                        imageHeightScale = EGymTheme.Dimens.exerciseHeadCard.imageHeight
                    )
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
                if (showShimmer.value) {
                    item {
                        ShimmerCardListItem2(
                            imageHeightScale = EGymTheme.Dimens.exerciseHeadCard.imageHeight
                        ) {
                            onIntent(ExerciseListIntent.FetchNextPage)
                        }
                    }
                }
            }
        }
    }
}