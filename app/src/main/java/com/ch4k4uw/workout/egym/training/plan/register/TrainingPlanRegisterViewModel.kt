package com.ch4k4uw.workout.egym.training.plan.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ch4k4uw.workout.egym.common.BaseAppStateViewModel
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.exercise.list.extensions.toView
import com.ch4k4uw.workout.egym.navigation.Screen
import com.ch4k4uw.workout.egym.training.plan.list.extensions.toView
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanView
import com.ch4k4uw.workout.egym.training.plan.list.interaction.toCreateDomain
import com.ch4k4uw.workout.egym.training.plan.list.interaction.toUpdateDomain
import com.ch4k4uw.workout.egym.training.plan.register.domain.interactor.TrainingPlanRegisterInteractor
import com.ch4k4uw.workout.egym.training.plan.register.interaction.TrainingPlanRegisterIntent
import com.ch4k4uw.workout.egym.training.plan.register.interaction.TrainingPlanRegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TrainingPlanRegisterViewModel @Inject constructor(
    private val trainingPlanRegisterInteractor: TrainingPlanRegisterInteractor,
    private val savedStateHandler: SavedStateHandle
) : BaseAppStateViewModel<TrainingPlanRegisterState, TrainingPlanRegisterIntent>() {
    private var exerciseHeadPager: ExerciseHeadPager? = null
    private val queryChannel = Channel<String>()
    private val queryFlow = flow {
        for (query in queryChannel) {
            val text = queryChannel.tryReceive().getOrNull() ?: query
            emit(text)
        }
    }.debounce(500)

    private val planSavedState: TrainingPlanView?
        get() = savedStateHandler.get<TrainingPlanView?>(
            Screen.Home.Plan.Register.ArgsNames.PlanMetadata
        )

    init {
        viewModelScope.launch {
            emitLoading()
            launch {
                queryFlow.collect {
                    findExercisesHeadsPager(query = it)
                }
            }
            val state = planSavedState
                ?.let { plan ->
                    TrainingPlanRegisterState.ShowPlan(plan = plan)
                }
                ?: TrainingPlanRegisterState.ShowPlan(plan = TrainingPlanView.Empty)
            emitSuccess(value = state)
            emitLoaded()
        }
    }

    private var isLoading = false
    private suspend fun findExercisesHeadsPager(query: String) {
        try {
            viewModelScope.launch {
                while (isLoading) yield()
                if (query.isNotBlank()) {
                    trainingPlanRegisterInteractor
                        .findExercisesHeadsPager(query = query)
                        .single()
                        .also { pager ->
                            exerciseHeadPager = pager
                        }
                    loadFirstPage()
                } else {
                    emitSuccess(
                        value = TrainingPlanRegisterState.ShowExerciseSuggestions(
                            exercises = listOf()
                        )
                    )
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun loadFirstPage() {
        exerciseHeadPager?.also { pager ->
            viewModelScope.launch {
                if (pager.index == -1 || pager.index < pager.count - 1) {
                    while (isLoading) yield()
                    isLoading = true
                    emitLoading(tag = TrainingPlanRegisterState.SearchTag)
                    pager
                        .next()
                        .catch {
                            emitLoaded(tag = TrainingPlanRegisterState.SearchTag)
                            emitError(cause = it)
                            Timber.e(it)
                            isLoading = false
                        }
                        .collect { nextPager ->
                            emitLoaded(tag = TrainingPlanRegisterState.SearchTag)
                            if (nextPager.items.isNotEmpty()) {
                                emitSuccess(
                                    value = TrainingPlanRegisterState.ShowExerciseSuggestions(
                                        exercises = nextPager.items.toView()
                                    )
                                )
                            } else {
                                loadFirstPage()
                            }
                            isLoading = false
                        }
                } else {
                    emitSuccess(
                        value = TrainingPlanRegisterState.ShowExerciseSuggestions(
                            exercises = listOf()
                        )
                    )
                }
            }
        }
    }

    override fun performIntent(intent: TrainingPlanRegisterIntent) {
        viewModelScope.launch {
            when (intent) {
                is TrainingPlanRegisterIntent.PerformExerciseQuery ->
                    performQuery(query = intent.query)
                is TrainingPlanRegisterIntent.SavePlan -> performPlanSaving(plan = intent.plan)
            }
        }
    }

    private fun performQuery(query: String) {
        queryChannel.trySend(query)
    }

    private suspend fun performPlanSaving(plan: TrainingPlanView) {
        emitLoading()
        with(trainingPlanRegisterInteractor) {
            if (planSavedState != null) {
                updatePlan(plan = plan.toUpdateDomain())
            } else {
                insertPlan(plan = plan.toCreateDomain())
            }
        }.catch { cause ->
            emitLoaded()
            emitError(cause = cause, tag = TrainingPlanRegisterState.SavePlanTag)
            Timber.e(cause)
        }.collect {
            emitLoaded()
            emitSuccess(
                value = TrainingPlanRegisterState.ShowPlanSuccessfulSavedMessage(plan = it.toView())
            )
        }
    }
}