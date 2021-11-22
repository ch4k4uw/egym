package com.ch4k4uw.workout.egym.core.exercise.domain.data

import com.ch4k4uw.workout.egym.core.exercise.domain.entity.ExerciseHead
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface ExerciseHeadPager {
    companion object {
        val Empty = object : ExerciseHeadPager {
            override val index: Int
                get() = 0
            override val count: Int
                get() = 0
            override val size: Int
                get() = 0
            override val items: List<ExerciseHead>
                get() = listOf()

            override suspend fun next(): Flow<ExerciseHeadPager> =
                flowOf(this)
        }
    }

    val index: Int
    val count: Int
    val size: Int
    val items: List<ExerciseHead>
    suspend fun next(): Flow<ExerciseHeadPager>
}