package com.ch4k4uw.workout.egym.training.plan.register.domain.interactor

import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExercisePagerOptions
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.core.exercise.domain.repository.ExerciseRepository
import com.ch4k4uw.workout.egym.core.training.plan.domain.entity.TrainingPlan
import com.ch4k4uw.workout.egym.core.training.plan.domain.repository.TrainingPlanCmdRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class TrainingPlanRegisterInteractor @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val trainingPlanRepository: TrainingPlanCmdRepository
) {
    suspend fun insertPlan(plan: TrainingPlan): Flow<TrainingPlan> =
        trainingPlanRepository.insert(entity = plan)

    suspend fun updatePlan(plan: TrainingPlan): Flow<TrainingPlan> =
        trainingPlanRepository.update(entity = plan)

    suspend fun findExercisesHeadsPager(
        query: String
    ): Flow<ExerciseHeadPager> =
        exerciseRepository.findHeadsPager(
            query = query,
            options = ExercisePagerOptions(size = 5)
        )
}