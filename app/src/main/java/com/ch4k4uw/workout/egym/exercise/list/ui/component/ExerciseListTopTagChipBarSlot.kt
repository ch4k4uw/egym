package com.ch4k4uw.workout.egym.exercise.list.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Surface
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseHeadView
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseListIntent
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode

@Composable
fun ExerciseListTopTagChipBarSlot(
    modifier: Modifier,
    queryText: State<String>,
    exerciseTags: MutableList<Pair<ExerciseTag, Boolean>>,
    showShimmer: MutableState<Boolean>,
    exercisesHeads: MutableList<ExerciseHeadView>,
    isLoadingStateForced: MutableState<Boolean>,
    onIntent: (ExerciseListIntent) -> Unit
) {
    Surface(
        modifier = modifier,
        color = AppTheme.colors.material.primarySurface,
        elevation = AppBarDefaults.TopAppBarElevation
    ) {
        FlowRow(
            modifier = Modifier
                .padding(all = AppTheme.Dimens.spacing.xtiny),
            mainAxisAlignment = MainAxisAlignment.Center,
            mainAxisSize = SizeMode.Expand,
            crossAxisSpacing = AppTheme.Dimens.spacing.small,
            mainAxisSpacing = AppTheme.Dimens.spacing.xtiny,
        ) {
            for (tag in exerciseTags) {
                ExerciseListTagChip(tag = tag.first, isSelected = tag.second) { rawTag, state ->
                    exerciseTags
                        .indexOfFirst { it.first == rawTag }
                        .takeIf { it != -1 }
                        ?.also { index ->
                            showShimmer.value = true
                            exercisesHeads.clear()
                            isLoadingStateForced.value = true
                            exerciseTags[index] = exerciseTags[index].copy(second = state)
                            onIntent(
                                ExerciseListIntent.PerformQuery(
                                    query = queryText.value,
                                    tags = exerciseTags.filter { it.second }.map { it.first }
                                )
                            )
                        }
                }
            }
        }
    }
}