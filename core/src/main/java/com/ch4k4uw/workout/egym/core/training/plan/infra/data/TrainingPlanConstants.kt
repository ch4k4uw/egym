package com.ch4k4uw.workout.egym.core.training.plan.infra.data

import android.content.Context
import com.ch4k4uw.workout.egym.core.R
import com.google.firebase.firestore.FieldPath

object TrainingPlanConstants {
    fun findTableName(context: Context): String =
        context.getString(R.string.table_training_plan)

    object Field {
        val id by lazy {
            FieldPath.documentId()
        }
        const val UserId = "uid"
        const val Title = "title"
        const val Description = "description"
        const val Created = "created"
        const val Updated = "updated"
    }
}