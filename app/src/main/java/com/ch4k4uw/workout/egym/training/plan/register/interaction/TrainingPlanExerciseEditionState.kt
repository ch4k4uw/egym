package com.ch4k4uw.workout.egym.training.plan.register.interaction

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

@Stable
class TrainingPlanExerciseEditionState(
    exercise: String = "",
    notes: String = "",
    sets: Int = 0,
    reps: Int = 0
) {
    private val mutableExercise = mutableStateOf(exercise)
    val exercise: State<String> = mutableExercise
    private val mutableNotes = mutableStateOf(notes)
    val notes: State<String> = mutableNotes
    private val mutableSets = mutableStateOf(sets)
    val sets: State<Int> = mutableSets
    private val mutableReps = mutableStateOf(reps)
    val reps: State<Int> = mutableReps

    fun update(
        exercise: String = this.exercise.value,
        notes: String = this.notes.value,
        sets: Int = this.sets.value,
        reps: Int = this.reps.value,
    ) {
        mutableExercise.value = exercise
        mutableNotes.value = notes
        mutableSets.value = sets
        mutableReps.value = reps
    }

    companion object {
        val Saver: Saver<TrainingPlanExerciseEditionState, *> = Saver(
            save = {
                Bundle().apply {
                    putString("ex", it.exercise.value)
                    putString("nt", it.notes.value)
                    putInt("st", it.sets.value)
                    putInt("rp", it.reps.value)
                }
            },
            restore = {
                TrainingPlanExerciseEditionState(
                    exercise = it.getString("ex") ?: "",
                    notes = it.getString("nt") ?: "",
                    sets = it.getInt("st"),
                    reps = it.getInt("rp"),
                )
            }
        )
    }
}

@Composable
fun rememberSaveableTrainingPlanExerciseEditionState(): TrainingPlanExerciseEditionState =
    rememberSaveable(saver = TrainingPlanExerciseEditionState.Saver) {
        TrainingPlanExerciseEditionState()
    }