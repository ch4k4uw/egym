package com.ch4k4uw.workout.egym.core.exercise.infra.injection

import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.infra.data.ExerciseHeadPagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class ExerciseDataModule {
    @Binds
    abstract fun bindExerciseHeadPager(impl: ExerciseHeadPagerImpl): ExerciseHeadPager
}