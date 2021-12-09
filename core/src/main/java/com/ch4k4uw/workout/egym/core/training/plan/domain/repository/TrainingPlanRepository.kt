package com.ch4k4uw.workout.egym.core.training.plan.domain.repository

import com.ch4k4uw.workout.egym.core.training.plan.domain.entity.TrainingPlan
import kotlinx.coroutines.flow.Flow

interface TrainingPlanRepository {
    suspend fun find(): Flow<List<TrainingPlan>>
}