package com.ch4k4uw.workout.egym.core.exercise.infra.data

import com.ch4k4uw.workout.egym.core.BuildConfig
import com.google.firebase.firestore.FieldPath

internal object ExerciseConstants {
    const val Name = BuildConfig.TABLE_EXERCISE
    object Field {
        val id by lazy {
            FieldPath.documentId()
        }
        const val Title = "title"
        const val Description = "description"
        const val Tags = "tags"
        const val Images = "images"
        const val Created = "created"
        const val Updated = "updated"
    }
}