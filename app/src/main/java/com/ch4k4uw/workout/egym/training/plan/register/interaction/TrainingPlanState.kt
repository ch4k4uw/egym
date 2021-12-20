package com.ch4k4uw.workout.egym.training.plan.register.interaction

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanView

@Stable
class TrainingPlanState {
    val id = mutableStateOf("")
    val title = mutableStateOf(TextFieldValue())
    val description = mutableStateOf(TextFieldValue())
    val exercises = mutableStateOf(listOf<TrainingPlanExerciseView>())
}

fun TrainingPlanState.toView(): TrainingPlanView =
    TrainingPlanView(
        id = id.value,
        title = title.value.text,
        description = description.value.text,
        exercises = exercises.value
    )

fun TrainingPlanState.fromView(view: TrainingPlanView): TrainingPlanState =
    also {
        view.apply {
            it.id.value = id
            it.title.value = TextFieldValue(
                text = title, selection = TextRange(start = 0, end = title.length)
            )
            it.description.value = TextFieldValue(
                text = description, selection = TextRange(start = 0, end = description.length)
            )
            it.exercises.value = exercises
        }
    }

private val trainingPlanStateSaver: Saver<TrainingPlanState, *> = Saver(
    save = {
        Bundle().apply {
            putString("id", it.id.value)
            putSerializable(
                "tt", Triple(
                    it.title.value.text,
                    it.title.value.selection.start,
                    it.title.value.selection.end
                )
            )
            putSerializable(
                "ds", Triple(
                    it.description.value.text,
                    it.description.value.selection.start,
                    it.description.value.selection.end
                )
            )
            putParcelableArray("ex", it.exercises.value.toTypedArray())
        }
    },
    restore = {
        TrainingPlanState().apply {
            id.value = it.getString("id") ?: ""
            title.value = it.getSerializable("tt")
                ?.run { this as? Triple<*, *, *> }
                ?.run {
                    TextFieldValue(
                        text = first as String,
                        TextRange(start = second as Int, end = third as Int)
                    )
                }
                ?: TextFieldValue()
            description.value = it.getSerializable("ds")
                ?.run { this as? Triple<*, *, *> }
                ?.run {
                    TextFieldValue(
                        text = first as String,
                        TextRange(start = second as Int, end = third as Int)
                    )
                }
                ?: TextFieldValue()
            exercises.value = it.getParcelableArray("ex")
                ?.run { map { item -> item as TrainingPlanExerciseView } }
                ?: listOf()
        }
    }
)

@Composable
fun rememberSaveableTrainingPlanState(): TrainingPlanState =
    rememberSaveable(saver = trainingPlanStateSaver) {
        TrainingPlanState()
    }