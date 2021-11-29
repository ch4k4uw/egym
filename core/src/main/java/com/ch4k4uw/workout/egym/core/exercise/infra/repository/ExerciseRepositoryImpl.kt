package com.ch4k4uw.workout.egym.core.exercise.infra.repository

import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExercisePagerOptions
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.core.exercise.domain.repository.ExerciseRepository
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.ExerciseSubComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseSubComponentFactory: ExerciseSubComponent.Factory
) : ExerciseRepository {
    override suspend fun findHeadsPager(
        query: String,
        tags: List<ExerciseTag>,
        options: ExercisePagerOptions
    ): Flow<ExerciseHeadPager> = flow {
        val component = exerciseSubComponentFactory
            .create(
                pageSize = options.size,
                queryString = query.takeIf { it.isNotBlank() },
                queryTags = tags.takeIf { it.isNotEmpty() }
            )
        emit(component.exerciseHeadPager)
    }
}
