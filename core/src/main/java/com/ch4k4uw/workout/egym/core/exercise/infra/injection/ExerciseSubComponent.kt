package com.ch4k4uw.workout.egym.core.exercise.infra.injection

import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.qualifier.ExerciseCollectionCount
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.qualifier.ExerciseCollectionPageSize
import dagger.BindsInstance
import dagger.Subcomponent
import dagger.hilt.android.scopes.ActivityScoped

@Subcomponent(
    modules = [
        ExerciseDataModule::class
    ]
)
@ActivityScoped
interface ExerciseSubComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @ExerciseCollectionCount
            @BindsInstance collectionCount: Int,
            @ExerciseCollectionPageSize
            @BindsInstance collectionPageSize: Int
        ) : ExerciseSubComponent
    }

    val exerciseHeadPager: ExerciseHeadPager
}