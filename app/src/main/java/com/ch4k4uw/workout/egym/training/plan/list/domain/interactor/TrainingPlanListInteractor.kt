package com.ch4k4uw.workout.egym.training.plan.list.domain.interactor

import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.core.auth.domain.repository.UserCmdRepository
import com.ch4k4uw.workout.egym.core.training.plan.domain.entity.TrainingPlan
import com.ch4k4uw.workout.egym.core.training.plan.domain.repository.TrainingPlanCmdRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TrainingPlanListInteractor @Inject constructor(
    private val userRepository: UserCmdRepository,
    private val trainingPlanRepository: TrainingPlanCmdRepository
) {
    suspend fun performLogout(): Flow<Unit> =
        userRepository.performLogout()

    suspend fun findLoggedUser(): Flow<User> =
        userRepository.findLoggedUser()

    suspend fun findTrainingPlanList(): Flow<List<TrainingPlan>> =
        trainingPlanRepository
            .find()

    suspend fun deleteTrainingPlan(id: String): Flow<Unit> =
        trainingPlanRepository
            .delete(id)
}