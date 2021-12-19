package com.ch4k4uw.workout.egym.exercise.list

import androidx.lifecycle.viewModelScope
import com.ch4k4uw.workout.egym.common.BaseAppStateViewModel
import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.core.exercise.domain.entity.ExerciseHead
import com.ch4k4uw.workout.egym.exercise.list.domain.interactor.ExerciseListInteractor
import com.ch4k4uw.workout.egym.exercise.list.extensions.toView
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseListIntent
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseListState
import com.ch4k4uw.workout.egym.login.extensions.toView
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
class ExerciseListViewModel @Inject constructor(
    private val exerciseListInteractor: ExerciseListInteractor
) : BaseAppStateViewModel<ExerciseListState, ExerciseListIntent>() {
    private var exerciseHeadPager: ExerciseHeadPager? = null
    private val exerciseHeadList = mutableListOf<ExerciseHead>()

    private val queryChannel = Channel<Pair<String, List<ExerciseTag>>>()
    private val queryFlow = flow {
        for (query in queryChannel) {
            val text = queryChannel.tryReceive().getOrNull() ?: query
            emit(text)
        }
    }.debounce(500)

    init {
        viewModelScope.launch {
            emitLoading()
            launch {
                queryFlow.collect {
                    findExercisesHeadsPager(query = it.first, tags = it.second)
                }
            }
            exerciseListInteractor
                .findLoggedUser()
                .catch {
                    emitLoaded()
                    emitError(cause = it)
                    Timber.e(it)
                }
                .collect { user ->
                    emitLoaded()
                    val state = if (user != User.Empty) {
                        queryChannel.send(Pair("", listOf()))
                        ExerciseListState.DisplayUserData(user = user.toView())
                    } else {
                        ExerciseListState.ShowLoginScreen
                    }
                    emitSuccess(value = state)
                    exerciseListInteractor
                        .findExerciseTags()
                        .catch {
                            emitError(cause = it)
                            it.printStackTrace()
                        }
                        .collect { tags ->
                            emitSuccess(value = ExerciseListState.ShowExerciseTagList(tags = tags))
                        }
                }
        }
    }

    private var isLoading = false
    private suspend fun findExercisesHeadsPager(query: String, tags: List<ExerciseTag>) {
        try {
            viewModelScope.launch {
                while (isLoading) yield()
                exerciseListInteractor
                    .findExercisesHeadsPager(query = query, tags = tags)
                    .single()
                    .also { pager ->
                        exerciseHeadPager = pager
                    }
                exerciseHeadList.clear()
                loadNextPage()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun loadNextPage() {
        exerciseHeadPager?.also { pager ->
            viewModelScope.launch {
                if (pager.index == -1 || pager.index < pager.count - 1) {
                    while (isLoading) yield()
                    isLoading = true
                    emitLoading(tag = ExerciseListState.ExerciseListTag)
                    pager
                        .next()
                        .catch {
                            emitLoaded(tag = ExerciseListState.ExerciseListTag)
                            emitError(cause = it)
                            Timber.e(it)
                            isLoading = false
                        }
                        .collect { nextPager ->
                            exerciseHeadList.addAll(nextPager.items)
                            emitLoaded(tag = ExerciseListState.ExerciseListTag)
                            if (nextPager.items.isNotEmpty()) {
                                emitSuccess(
                                    value = ExerciseListState.ShowExerciseList(
                                        exercises = exerciseHeadList.toView()
                                    )
                                )
                            } else {
                                loadNextPage()
                            }
                            isLoading = false
                        }
                } else {
                    emitSuccess(value = ExerciseListState.ShowNoMorePagesToFetch)
                }
            }
        }
    }

    private fun performQuery(query: String = "", tags: List<ExerciseTag> = listOf()) {
        queryChannel.trySend(Pair(query, tags))
    }

    override fun performIntent(intent: ExerciseListIntent) {
        when (intent) {
            is ExerciseListIntent.PerformLogout -> performLogout()
            is ExerciseListIntent.FetchNextPage -> loadNextPage()
            is ExerciseListIntent.PerformQuery -> performQuery(
                query = intent.query, tags = intent.tags
            )
        }
    }

    private fun performLogout() {
        viewModelScope.launch {
            emitLoading()
            exerciseListInteractor.performLogout()
                .catch {
                    emitLoaded()
                    emitError(it)
                    Timber.e(it)
                }
                .collect {
                    emitLoaded()
                    emitSuccess(ExerciseListState.ShowLoginScreen)
                }
        }
    }
}