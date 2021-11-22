package com.ch4k4uw.workout.egym.home.domain.interactor

import com.ch4k4uw.workout.egym.core.auth.domain.repository.UserCmdRepository
import com.ch4k4uw.workout.egym.core.exercise.domain.repository.ExerciseRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class HomeInteractor @Inject constructor(
    private val userRepository: UserCmdRepository,
    private val exerciseRepository: ExerciseRepository
) {
    suspend fun performLogout(): Flow<Unit> =
        userRepository.performLogout()
}