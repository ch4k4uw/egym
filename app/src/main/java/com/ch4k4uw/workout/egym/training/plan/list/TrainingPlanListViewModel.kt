package com.ch4k4uw.workout.egym.training.plan.list

import androidx.lifecycle.viewModelScope
import com.ch4k4uw.workout.egym.common.BaseAppStateViewModel
import com.ch4k4uw.workout.egym.core.training.plan.domain.repository.TrainingPlanRepository
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanListIntent
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class TrainingPlanListViewModel @Inject constructor(
    private val trainingPlanRepository: TrainingPlanRepository
) : BaseAppStateViewModel<TrainingPlanListState, TrainingPlanListIntent>() {
    init {
        viewModelScope.launch {
            emitLoading()
            trainingPlanRepository
                .find()
                .catch { cause ->
                    emitLoaded()
                    Timber.e(cause)
                    emitError(cause = cause)
                }
                .collect {
                    emitLoaded()
                    val state = if (it.isEmpty()) {
                        TrainingPlanListState.DisplayNoTrainingPlansToShowMessage
                    } else {
                        TrainingPlanListState.ShowTrainingPlanList(plans = it)
                    }
                    emitSuccess(value = state)
                }
        }
    }
}