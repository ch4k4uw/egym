package com.ch4k4uw.workout.egym.core.exercise.infra.data

import com.ch4k4uw.workout.egym.core.BuildConfig
import com.ch4k4uw.workout.egym.core.common.infra.service.FindPagedFirebaseDocumentService
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.domain.entity.ExerciseHead
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.qualifier.ExerciseCollectionCount
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.qualifier.ExerciseCollectionPageSize
import com.ch4k4uw.workout.egym.core.extensions.asLocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExerciseHeadPagerImpl @Inject constructor(
    @ExerciseCollectionCount
    collectionCount: Int,
    @ExerciseCollectionPageSize
    override val size: Int,
    private val findPagedDocSvc: FindPagedFirebaseDocumentService
) : ExerciseHeadPager {
    override val count: Int by lazy {
        (collectionCount / size) + if (collectionCount.rem(size) != 0) 1 else 0
    }

    override var index: Int = 0
        private set

    override var items: List<ExerciseHead> = listOf()
        private set

    override suspend fun next(): Flow<ExerciseHeadPager> = flow {
        if (index < count) {
            items = findNextPage()
            ++index
        }
        emit(this@ExerciseHeadPagerImpl)
    }

    private suspend fun findNextPage(): List<ExerciseHead> =
        findPagedDocSvc.find(
            collection = BuildConfig.TABLE_EXERCISE,
            orderBy = ExerciseConstants.Field.Title,
            lastDoc = if (items.isNotEmpty()) {
               items[size - 1].title
            } else {
               Unit
            },
            pageSize = size,
        ) {
            ExerciseHead(
                id = id,
                title = getString(ExerciseConstants.Field.Title).orEmpty(),
                image = getString(ExerciseConstants.Field.Images).orEmpty(),
                created = getLong(ExerciseConstants.Field.Created).asLocalDateTime,
                updated = getLong(ExerciseConstants.Field.Updated).asLocalDateTime,
            )
        }

}