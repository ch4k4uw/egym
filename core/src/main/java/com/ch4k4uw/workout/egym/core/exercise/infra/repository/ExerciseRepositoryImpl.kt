package com.ch4k4uw.workout.egym.core.exercise.infra.repository

import android.content.Context
import com.ch4k4uw.workout.egym.core.common.infra.AppDispatchers
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseHeadPager
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExercisePagerOptions
import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.core.exercise.domain.entity.Exercise
import com.ch4k4uw.workout.egym.core.exercise.domain.repository.ExerciseRepository
import com.ch4k4uw.workout.egym.core.exercise.infra.data.ExerciseConstants
import com.ch4k4uw.workout.egym.core.exercise.infra.injection.ExerciseSubComponent
import com.ch4k4uw.workout.egym.core.extensions.asLocalDateTime
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ExerciseRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val exerciseSubComponentFactory: ExerciseSubComponent.Factory,
    private val appDispatchers: AppDispatchers
) : ExerciseRepository {
    private val db = Firebase.firestore

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

    override suspend fun findById(id: String): Flow<Exercise> = flow {
        val collRef = db.collection(
            ExerciseConstants.findTableName(context = context)
        )
        val docRef = collRef.document(id)
        val exercise = suspendCancellableCoroutine<Exercise> { continuation ->
            docRef
                .get()
                .addOnSuccessListener { document ->
                    (document.data as? Map<String, Any?>?)?.also { rawData ->
                        continuation.resume(
                            Exercise(
                                id = document.id,
                                title = rawData[ExerciseConstants.Field.Title]
                                    ?.toString().orEmpty(),
                                description = rawData[ExerciseConstants.Field.Description]
                                    ?.toString().orEmpty(),
                                tags = rawData[ExerciseConstants.Field.Tags]
                                    ?.let { it as? List<*>? }
                                    ?.mapNotNull { it?.toString() }
                                    ?.mapNotNull { ExerciseTag.values().find { tag ->
                                        tag.raw == it }
                                    } ?: listOf(),
                                images = rawData[ExerciseConstants.Field.Images]
                                    ?.let { it as? List<*>? }
                                    ?.mapNotNull { it?.toString() }
                                    ?: listOf(),
                                created = rawData[ExerciseConstants.Field.Created]
                                    ?.let { it as? Long }
                                    ?.let { it.asLocalDateTime } ?: LocalDateTime.MIN,
                                updated = rawData[ExerciseConstants.Field.Updated]
                                    ?.let { it as? Long }
                                    ?.let { it.asLocalDateTime } ?: LocalDateTime.MIN,
                            )
                        )
                    } ?: run {
                        continuation.resume(Exercise.Empty)
                    }
                }
                .addOnFailureListener { cause ->
                    continuation.resumeWithException(exception = cause)
                }
                .addOnCanceledListener {
                    continuation.cancel()
                }
        }
        emit(exercise)
    }.flowOn(appDispatchers.io)
}
