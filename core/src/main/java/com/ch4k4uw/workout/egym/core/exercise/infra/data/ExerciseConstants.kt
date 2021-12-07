package com.ch4k4uw.workout.egym.core.exercise.infra.data

import android.content.Context
import com.ch4k4uw.workout.egym.core.R
import com.google.firebase.firestore.FieldPath

internal object ExerciseConstants {
    fun findTableName(context: Context) =
        context.getString(R.string.table_exercise)

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