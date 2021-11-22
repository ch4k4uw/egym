package com.ch4k4uw.workout.egym.core.exercise.infra.repository

import com.ch4k4uw.workout.egym.core.BuildConfig
import com.ch4k4uw.workout.egym.core.common.infra.service.HelperApi
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExercisePagerOptions
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.core.exercise.domain.repository.ExerciseRepository
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.ExerciseSubComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val helperApi: HelperApi,
    private val exerciseSubComponentFactory: ExerciseSubComponent.Factory
) : ExerciseRepository {
    override suspend fun findHeads(
        query: String,
        tags: List<ExerciseTag>,
        options: ExercisePagerOptions
    ): Flow<ExerciseHeadPager> = flow {
        val count = findCollectionCount()
        val component = exerciseSubComponentFactory
            .create(
                collectionCount = count,
                collectionPageSize = options.size
            )
        emit(component.exerciseHeadPager)
    }

    private suspend fun findCollectionCount(): Int =
        helperApi.findCollectionCount(BuildConfig.TABLE_EXERCISE)
            .count
}
