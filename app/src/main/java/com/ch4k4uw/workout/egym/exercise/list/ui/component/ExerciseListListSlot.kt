package com.ch4k4uw.workout.egym.exercise.list.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseHeadView
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseListIntent
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseListState
import com.ch4k4uw.workout.egym.extensions.asLoading
import com.ch4k4uw.workout.egym.common.state.AppState
import com.ch4k4uw.workout.egym.core.ui.AppTheme

@ExperimentalUnitApi
@Composable
fun ExerciseListListSlot(
    modifier: Modifier,
    uiState: State<AppState<ExerciseListState>>,
    isLoadingStateForced: MutableState<Boolean>,
    exercisesHeads: SnapshotStateList<ExerciseHeadView>,
    showShimmer: MutableState<Boolean>,
    onExerciseClick: (String) -> Unit,
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
                    Column {
                        ExerciseListHeadCard(
                            imageUrl = exercisesHeads[index].image,
                            title = exercisesHeads[index].title,
                            onClick = {
                                onExerciseClick(exercisesHeads[index].id)
                            }
                        )
                        if (index < exercisesHeads.size - 1) {
                            Spacer(modifier = Modifier.height(AppTheme.Dimens.spacing.xtiny))
                        }
                    }
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