package com.ch4k4uw.workout.egym.training.plan.register.interaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import com.ch4k4uw.workout.egym.training.plan.list.interaction.TrainingPlanView

@Stable
class TrainingPlanState(
    private val source: State<TrainingPlanView>
) {
    val id = derivedStateOf { source.value.id }
    val title = derivedStateOf { source.value.title }
    val description = derivedStateOf { source.value.description }
    val exercises = derivedStateOf { source.value.exercises }
}

@Composable
fun rememberTrainingPlanState(source: State<TrainingPlanView>): TrainingPlanState =
    remember(source.value) {
        TrainingPlanState(source = source)
    }