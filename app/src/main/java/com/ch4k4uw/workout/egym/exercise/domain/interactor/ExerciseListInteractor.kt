package com.ch4k4uw.workout.egym.exercise.domain.interactor

import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.core.auth.domain.repository.UserCmdRepository
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExercisePagerOptions
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.core.exercise.domain.repository.ExerciseRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class ExerciseListInteractor @Inject constructor(
    private val userRepository: UserCmdRepository,
    private val exerciseRepository: ExerciseRepository
) {
    suspend fun performLogout(): Flow<Unit> =
        userRepository.performLogout()

    suspend fun findExercisesHeads(
        query: String = "",
        tags: List<ExerciseTag> = listOf()
    ): Flow<ExerciseHeadPager> =
        exerciseRepository.findHeads(
            query = query,
            tags = tags,
            options = ExercisePagerOptions(size = 5)
        )

    suspend fun findLoggedUser(): Flow<User> =
        userRepository.findLoggedUser()
}