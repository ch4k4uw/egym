package com.ch4k4uw.workout.egym.exercise.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.core.exercise.domain.entity.ExerciseHead
import com.ch4k4uw.workout.egym.exercise.list.domain.interactor.ExerciseListInteractor
import com.ch4k4uw.workout.egym.exercise.list.extensions.toView
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseListIntent
import com.ch4k4uw.workout.egym.exercise.list.interaction.ExerciseListState
import com.ch4k4uw.workout.egym.login.extensions.toView
import com.ch4k4uw.workout.egym.common.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    private val exerciseListInteractor: ExerciseListInteractor
) : ViewModel() {
    private val mutableUiState = MutableSharedFlow<AppState<ExerciseListState>>(replay = 1)
    val uiState: Flow<AppState<ExerciseListState>> = mutableUiState

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
            emit(AppState.Loading())
            launch {
                queryFlow.collect {
                    findExercisesHeadsPager(query = it.first, tags = it.second)
                }
            }
            exerciseListInteractor
                .findLoggedUser()
                .catch {
                    emit(AppState.Loaded())
                    emitError(cause = it)
                    it.printStackTrace()
                }
                .collect { user ->
                    emit(AppState.Loaded())
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
                            emit(AppState.Loaded())
                            emitError(cause = it)
                            it.printStackTrace()
                        }
                        .collect { tags ->
                            emitSuccess(value = ExerciseListState.ShowExerciseTagList(tags = tags))
                        }
                }
        }
    }

    private suspend fun <T : AppState<ExerciseListState>> emit(value: T) =
        mutableUiState.emit(value)

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
                    emit(AppState.Loading(tag = ExerciseListState.ExerciseListTag))
                    pager
                        .next()
                        .catch {
                            emit(AppState.Loaded(tag = ExerciseListState.ExerciseListTag))
                            emit(AppState.Error(cause = it))
                            it.printStackTrace()
                            isLoading = false
                        }
                        .collect { nextPager ->
                            exerciseHeadList.addAll(nextPager.items)
                            emit(AppState.Loaded(tag = ExerciseListState.ExerciseListTag))
                            if (nextPager.items.isNotEmpty()) {
                                emitSuccess(
                                    value = ExerciseListState.ShowExerciseList(
                                        exercises = exerciseHeadList.map { it.toView() }
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

    private suspend fun emitError(cause: Throwable) =
        emit(AppState.Error(cause))

    private suspend fun <T : ExerciseListState> emitSuccess(value: T) =
        emit(AppState.Success(value))

    private fun performQuery(query: String = "", tags: List<ExerciseTag> = listOf()) {
        queryChannel.trySend(Pair(query, tags))
    }

    fun performIntent(intent: ExerciseListIntent) {
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
            emit(AppState.Loading())
            exerciseListInteractor.performLogout()
                .catch {
                    emit(AppState.Loaded())
                    emitError(it)
                    it.printStackTrace()
                }
                .collect {
                    emit(AppState.Loaded())
                    emitSuccess(ExerciseListState.ShowLoginScreen)
                }
        }
    }
}