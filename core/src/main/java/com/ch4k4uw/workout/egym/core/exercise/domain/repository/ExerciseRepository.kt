package com.ch4k4uw.workout.egym.core.exercise.domain.repository

import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExercisePagerOptions
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.core.exercise.domain.entity.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    suspend fun findHeadsPager(
        query: String = "",
        tags: List<ExerciseTag> = listOf(),
        options: ExercisePagerOptions = ExercisePagerOptions.Default
    ): Flow<ExerciseHeadPager>

    suspend fun findById(id: String): Flow<Exercise>
}