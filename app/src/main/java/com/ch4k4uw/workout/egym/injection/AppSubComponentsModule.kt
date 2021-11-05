package com.ch4k4uw.workout.egym.injection

import com.ch4k4uw.workout.egym.core.auth.infra.injection.FirebaseSubComponent
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module(
    subcomponents = [
        FirebaseSubComponent::class
    ]
)
@InstallIn(ViewModelComponent::class)
class AppSubComponentsModule