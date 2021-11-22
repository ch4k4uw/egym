package com.ch4k4uw.workout.egym.core.common.infra.service

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FindPagedFirebaseDocumentService @Inject constructor() {
    private val db by lazy {
        Firebase.firestore
    }

    suspend fun <T> find(
        collection: String,
        orderBy: String,
        lastDoc: Any? = null,
        pageSize: Int,
        docMapper: DocumentSnapshot.() -> T
    ): List<T> = suspendCancellableCoroutine { continuation ->
        db.collection(collection)
            .orderBy(orderBy)
            .let {
                if (lastDoc != null) {
                    it.startAfter(lastDoc)
                } else {
                    it
                }
            }
            .limit(pageSize.toLong())
            .get()
            .addOnSuccessListener { snapShot ->
                snapShot.documents.map { doc ->
                    docMapper(doc)
                }
            }
            .addOnFailureListener { cause ->
                continuation.resumeWith(Result.failure(cause))
            }
            .addOnCanceledListener {
                continuation.cancel()
            }
    }
}