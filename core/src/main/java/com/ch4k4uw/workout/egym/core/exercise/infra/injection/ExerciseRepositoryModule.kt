package com.ch4k4uw.workout.egym.core.exercise.infra.injection

import com.ch4k4uw.workout.egym.core.exercise.domain.repository.ExerciseRepository
import com.ch4k4uw.workout.egym.core.exercise.infra.repository.ExerciseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ExerciseRepositoryModule {
    @Binds
    @ViewModelScoped
    abstract fun bindExerciseHeadRepository(
        impl: ExerciseRepositoryImpl
    ): ExerciseRepository
}