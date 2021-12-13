package com.ch4k4uw.workout.egym.training.plan.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ch4k4uw.workout.egym.common.BaseAppStateViewModel
import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.login.extensions.toView
import com.ch4k4uw.workout.egym.training.plan.list.domain.interactor.TrainingPlanListInteractor
import com.ch4k4uw.workout.egym.training.plan.list.extensions.toView
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanListIntent
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanListState
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TrainingPlanListViewModel @Inject constructor(
    private val trainingPlanListInteractor: TrainingPlanListInteractor,
    private val savedStateHandle: SavedStateHandle
) : BaseAppStateViewModel<TrainingPlanListState, TrainingPlanListIntent>() {
    private var trainingPlanList: Array<TrainingPlanView>
        get() = savedStateHandle.get<Array<TrainingPlanView>>("list") ?: arrayOf()
        set(list) {
            savedStateHandle.set("list", list)
        }

    init {
        viewModelScope.launch {
            emitLoading()
            findLoggedUser()
            emitLoaded()
            emitLoading()
            findTrainingPlanList()
            emitLoaded()
        }
    }


    private suspend fun findLoggedUser() {
        trainingPlanListInteractor
            .findLoggedUser()
            .catch {
                emitError(cause = it, tag = TrainingPlanListState.FetchUserDataTag)
                Timber.e(it)
            }
            .collect { user ->
                val state = if (user != User.Empty) {
                    TrainingPlanListState.DisplayUserData(user = user.toView())
                } else {
                    TrainingPlanListState.ShowLoginScreen
                }
                emitSuccess(value = state)
            }
    }

    private suspend fun findTrainingPlanList() {
        trainingPlanListInteractor
            .findTrainingPlanList()
            .catch { cause ->
                Timber.e(cause)
                emitError(cause = cause, tag = TrainingPlanListState.FetchPlanListTag)
            }
            .collect {
                val state = if (it.isEmpty()) {
                    TrainingPlanListState.DisplayNoPlansToShowMessage
                } else {
                    TrainingPlanListState.ShowPlanList(
                        plans = it.toView().apply {
                            trainingPlanList = toTypedArray()
                        }
                    )
                }
                emitSuccess(value = state)
            }
    }

    override fun performIntent(intent: TrainingPlanListIntent) {
        when (intent) {
            is TrainingPlanListIntent.PerformLogout -> viewModelScope.launch {
                emitLoading()
                performLogout()
                emitLoaded()
            }
            is TrainingPlanListIntent.FetchUserData -> viewModelScope.launch {
                emitLoading()
                findLoggedUser()
                emitLoaded()
            }
            is TrainingPlanListIntent.FetchPlanList -> viewModelScope.launch {
                emitLoading()
                findTrainingPlanList()
                emitLoaded()
            }
            is TrainingPlanListIntent.DeletePlan -> {
                viewModelScope.launch {
                    if (intent.confirmed) {
                        emitLoading()
                        deletePlan(id = intent.plan.id)
                        emitLoaded()
                    } else {
                        emitSuccess(
                            value = TrainingPlanListState.ConfirmPlanDeletion(plan = intent.plan)
                        )
                    }
                }
            }
        }
    }

    private suspend fun performLogout() {
        trainingPlanListInteractor
            .performLogout()
            .catch { cause ->
                Timber.e(cause)
                emitError(cause = cause, tag = TrainingPlanListState.PerformLogoutTag)
            }
            .collect {
                emitSuccess(value = TrainingPlanListState.ShowLoginScreen)
            }
    }

    private suspend fun deletePlan(id: String) {
        trainingPlanListInteractor
            .deleteTrainingPlan(id = id)
            .catch { cause ->
                Timber.e(cause)
                emitError(cause = cause, tag = TrainingPlanListState.DeletePlanTag)
            }
            .collect {
                val state = if (trainingPlanList.size - 1 > 0) {
                    TrainingPlanListState.ShowPlanList(
                        plans = trainingPlanList.run {
                            filter { it.id != id }.apply {
                                trainingPlanList = toTypedArray()
                            }
                        }
                    )
                } else {
                    TrainingPlanListState.DisplayNoPlansToShowMessage
                }
                emitSuccess(
                    value = state
                )
            }
    }
}