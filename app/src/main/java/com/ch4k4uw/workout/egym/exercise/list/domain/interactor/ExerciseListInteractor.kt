package com.ch4k4uw.workout.egym.exercise.list.domain.interactor

import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.core.auth.domain.repository.UserCmdRepository
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExercisePagerOptions
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.core.exercise.domain.repository.ExerciseRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ViewModelScoped
class ExerciseListInteractor @Inject constructor(
    private val userRepository: UserCmdRepository,
    private val exerciseRepository: ExerciseRepository
) {
    suspend fun performLogout(): Flow<Unit> =
        userRepository.performLogout()

    suspend fun findExercisesHeadsPager(
        query: String = "",
        tags: List<ExerciseTag> = listOf()
    ): Flow<ExerciseHeadPager> =
        exerciseRepository.findHeadsPager(
            query = query,
            tags = tags
        )

    suspend fun findLoggedUser(): Flow<User> =
        userRepository.findLoggedUser()

    suspend fun findExerciseTags(): Flow<List<ExerciseTag>> =
        flowOf(ExerciseTag.values().toList())
}