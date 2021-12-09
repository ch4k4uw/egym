package com.ch4k4uw.workout.egym.core.training.plan.domain.repository

import com.ch4k4uw.workout.egym.core.training.plan.domain.entity.TrainingPlan
import kotlinx.coroutines.flow.Flow

interface TrainingPlanCmdRepository : TrainingPlanRepository {
    suspend fun insert(entity: TrainingPlan): Flow<TrainingPlan>
    suspend fun update(entity: TrainingPlan): Flow<TrainingPlan>
    suspend fun delete(id: String): Flow<Unit>
}