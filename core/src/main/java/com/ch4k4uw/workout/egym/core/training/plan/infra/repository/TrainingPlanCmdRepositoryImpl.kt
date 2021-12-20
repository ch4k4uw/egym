package com.ch4k4uw.workout.egym.core.training.plan.infra.repository

import android.content.Context
import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.core.auth.domain.repository.UserRepository
import com.ch4k4uw.workout.egym.core.common.infra.AppDispatchers
import com.ch4k4uw.workout.egym.core.extensions.asMilliseconds
import com.ch4k4uw.workout.egym.core.training.plan.domain.entity.TrainingPlan
import com.ch4k4uw.workout.egym.core.training.plan.domain.repository.TrainingPlanCmdRepository
import com.ch4k4uw.workout.egym.core.training.plan.infra.data.TrainingPlanConstants
import com.ch4k4uw.workout.egym.core.training.plan.infra.entity.TrainingPlanRemote
import com.ch4k4uw.workout.egym.core.training.plan.infra.entity.toDomain
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@ExperimentalCoroutinesApi
class TrainingPlanCmdRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
    private val appDispatchers: AppDispatchers
) : TrainingPlanCmdRepository {
    private val db = Firebase.firestore
    private val collRef by lazy {
        db.collection(TrainingPlanConstants.findTableName(context = context))
    }

    override suspend fun find(): Flow<List<TrainingPlan>> = flow {
        val user = findLoggedUser()
        val entities = suspendCancellableCoroutine<List<TrainingPlan>> { continuation ->
            collRef
                .whereEqualTo(TrainingPlanConstants.Field.UserId, user.id)
                .orderBy(TrainingPlanConstants.Field.Created)
                .get()
                .addOnSuccessListener { snapshot ->
                    try {
                        snapshot
                            .documents
                            .mapNotNull { doc ->
                                doc.toObject(TrainingPlanRemote::class.java)
                                    ?.also { it.id = doc.id }
                                    ?.toDomain()
                            }
                            .also {
                                continuation.resume(value = it)
                            }
                    } catch (cause: Throwable) {
                        continuation.resumeWithException(cause)
                    }
                }
                .addOnFailureListener(continuation::resumeWithException)
                .addOnCanceledListener(continuation::cancel)
        }

        emit(entities)
    }.flowOn(appDispatchers.io)

    private suspend fun findLoggedUser(): User =
        userRepository.findLoggedUser().single().also {
            check(it != User.Empty)
        }

    override suspend fun insert(entity: TrainingPlan): Flow<TrainingPlan> = flow {
        val user = findLoggedUser()
        val doc = collRef.document()
        val resultEntity = suspendCancellableCoroutine<TrainingPlan> { continuation ->
            val now = LocalDateTime.now()
            val newEntity = entity.copy(
                id = doc.id,
                title = entity.title,
                description = entity.description,
                exercises = entity.exercises,
                created = now,
                updated = now
            )

            doc
                .set(
                    newEntity.toRemoteCreation(userId = user.id)
                )
                .addOnSuccessListener {
                    continuation.resume(value = newEntity)
                }
                .addOnFailureListener(continuation::resumeWithException)
                .addOnCanceledListener(continuation::cancel)
        }
        emit(resultEntity)
    }.flowOn(appDispatchers.io)

    private fun TrainingPlan.toRemoteCreation(userId: String): HashMap<String, Any> =
        hashMapOf(
            TrainingPlanConstants.Field.UserId to userId,
            TrainingPlanConstants.Field.Title to title,
            TrainingPlanConstants.Field.Description to description,
            TrainingPlanConstants.Field.Exercises to exercises,
            TrainingPlanConstants.Field.Created to created.asMilliseconds,
            TrainingPlanConstants.Field.Updated to updated.asMilliseconds,
        )

    override suspend fun update(entity: TrainingPlan): Flow<TrainingPlan> = flow {
        check(entity.id.isNotBlank())
        val doc = collRef.document(entity.id)
        val resultEntity = suspendCancellableCoroutine<TrainingPlan> { continuation ->
            val now = LocalDateTime.now()
            val newEntity = entity.copy(
                id = doc.id,
                title = entity.title,
                description = entity.description,
                exercises = entity.exercises,
                updated = now
            )

            doc
                .set(
                    newEntity.toRemoteUpdate(),
                    SetOptions.merge()
                )
                .addOnSuccessListener {
                    continuation.resume(value = newEntity)
                }
                .addOnFailureListener(continuation::resumeWithException)
                .addOnCanceledListener(continuation::cancel)
        }
        emit(resultEntity)
    }.flowOn(appDispatchers.io)

    private fun TrainingPlan.toRemoteUpdate(): HashMap<String, Any> =
        hashMapOf(
            TrainingPlanConstants.Field.Title to title,
            TrainingPlanConstants.Field.Description to description,
            TrainingPlanConstants.Field.Exercises to exercises,
            TrainingPlanConstants.Field.Updated to updated.asMilliseconds,
        )

    override suspend fun delete(id: String): Flow<Unit> = flow {
        check(id.isNotBlank())
        val doc = collRef.document(id)
        suspendCancellableCoroutine<Unit> { continuation ->
            doc
                .delete()
                .addOnSuccessListener {
                    continuation.resume(value = Unit)
                }
                .addOnFailureListener(continuation::resumeWithException)
                .addOnCanceledListener(continuation::cancel)
        }
        emit(Unit)
    }.flowOn(appDispatchers.io)
}