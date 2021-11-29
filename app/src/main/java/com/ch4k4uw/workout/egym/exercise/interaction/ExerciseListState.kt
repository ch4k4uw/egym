package com.ch4k4uw.workout.egym.exercise.interaction

import com.ch4k4uw.workout.egym.login.interaction.UserView

sealed class ExerciseListState {
    data class DisplayUserData(val user: UserView): ExerciseListState()
    object ShowLoginScreen : ExerciseListState()
    object ExerciseListTag : ExerciseListState()
    data class ShowExerciseList(val exercises: List<ExerciseHeadView>): ExerciseListState()
}
