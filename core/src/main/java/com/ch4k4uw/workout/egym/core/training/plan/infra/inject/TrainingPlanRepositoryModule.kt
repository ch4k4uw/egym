package com.ch4k4uw.workout.egym.core.training.plan.infra.inject

import com.ch4k4uw.workout.egym.core.training.plan.domain.repository.TrainingPlanCmdRepository
import com.ch4k4uw.workout.egym.core.training.plan.domain.repository.TrainingPlanRepository
import com.ch4k4uw.workout.egym.core.training.plan.infra.repository.TrainingPlanCmdRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Module
@InstallIn(ViewModelComponent::class)
abstract class TrainingPlanRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindTrainingPlanRepository(
        impl: TrainingPlanCmdRepositoryImpl
    ): TrainingPlanRepository

    @Binds
    @ViewModelScoped
    abstract fun bindTrainingPlanCmdRepository(
        impl: TrainingPlanCmdRepositoryImpl
    ): TrainingPlanCmdRepository
}