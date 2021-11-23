package com.ch4k4uw.workout.egym.core.exercise.infra.injection

import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.qualifier.ExerciseHeadPageSize
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.qualifier.ExerciseHeadQueryString
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.qualifier.ExerciseHeadQueryTags
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
            @ExerciseHeadPageSize
            @BindsInstance pageSize: Int,
            @ExerciseHeadQueryString
            @BindsInstance queryString: String? = null,
            @ExerciseHeadQueryTags
            @BindsInstance queryTags: List<ExerciseTag>? = null,
        ) : ExerciseSubComponent
    }

    val exerciseHeadPager: ExerciseHeadPager
}