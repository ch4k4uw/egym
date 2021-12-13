package com.ch4k4uw.workout.egym.training.plan.list.interaction

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.ch4k4uw.workout.egym.R

@Immutable
interface TrainingPlanDeletionResources {
    val id: Int
    val title: String
    fun message(planTitle: String): String
    val positiveButtonLabel: String
    val negativeButtonLabel: String
}

@Immutable
private class ResourcesImpl(
    private val context: Context
) : TrainingPlanDeletionResources {
    override val id: Int
        get() = R.id.training_plan_delete_confirmation
    override val title: String
        get() = context.getString(R.string.training_plan_delete_plan_warning_title)
    override fun message(planTitle: String): String =
        context.getString(R.string.training_plan_delete_plan_warning_message, planTitle)
    override val positiveButtonLabel: String
        get() = context.getString(R.string.training_plan_delete_plan_warning_positive_button)
    override val negativeButtonLabel: String
        get() = context.getString(R.string.training_plan_delete_plan_warning_negative_button)
}

@Composable
fun rememberTrainingPlanDeletionResources(): TrainingPlanDeletionResources {
    val context = LocalContext.current
    return remember(key1 = LocalContext.current) { ResourcesImpl(context = context) }
}
