package com.ch4k4uw.workout.egym.core.common.infra.injection

import com.ch4k4uw.workout.egym.core.auth.infra.injection.FirebaseSubComponent
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.ExerciseSubComponent
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module(
    subcomponents = [
        FirebaseSubComponent::class,
        ExerciseSubComponent::class
    ]
)
@InstallIn(ViewModelComponent::class)
class CoreViewModelSubComponents