package com.ch4k4uw.workout.egym.core.exercise.infra.data

import com.ch4k4uw.workout.egym.core.common.infra.AppDispatchers
import com.ch4k4uw.workout.egym.core.common.infra.service.HelperApi
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.core.exercise.domain.entity.ExerciseHead
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.qualifier.ExerciseHeadPageSize
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.qualifier.ExerciseHeadQueryString
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.qualifier.ExerciseHeadQueryTags
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.time.ZoneId
import javax.inject.Inject

class ExerciseHeadPagerImpl @Inject constructor(
    private val helperApi: HelperApi,
    @ExerciseHeadPageSize
    override val size: Int,
    @ExerciseHeadQueryString
    private val queryString: String?,
    @ExerciseHeadQueryTags
    private val queryTags: List<ExerciseTag>?,
    private val appDispatchers: AppDispatchers
) : ExerciseHeadPager {
    private var collectionCount = 0

    override var count: Int = 0
        private set

    override var index: Int = -1
        private set

    override var items: List<ExerciseHead> = listOf()
        private set

    override suspend fun next(): Flow<ExerciseHeadPager> = flow {
        if (index < count) {
            findNextPage()
        }
        emit(this@ExerciseHeadPagerImpl)
    }

    private suspend fun findNextPage() {
        if (index == -1) {
            withContext(appDispatchers.io) {
                helperApi
                    .findExerciseHeadPager(
                        pageSize = size,
                        queryString = queryString,
                        queryTags = queryTags?.map { it.raw }
                    ).also(::updateData)
            }
        } else {
            withContext(appDispatchers.io) {
                helperApi
                    .findNextExerciseHeadPage(
                        pageSize = size,
                        collectionCount = collectionCount,
                        pageIndex = index,
                        lastItemTitle = items.last().title,
                        lastItemTime = items.last().created.atZone(ZoneId.systemDefault())
                            .toInstant().toEpochMilli(),
                        pageCount = count,
                        queryString = queryString,
                        queryTags = queryTags?.map { it.raw }
                    ).also(::updateData)
            }
        }
    }

    private fun updateData(pagerRemote: ExerciseHeadPagerRemote) {
        index = pagerRemote.index
        count = pagerRemote.count
        collectionCount = pagerRemote.itemsCount
        items = pagerRemote.items.map { it.toDomain() }
    }

}