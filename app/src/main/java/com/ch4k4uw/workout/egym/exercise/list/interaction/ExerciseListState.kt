package com.ch4k4uw.workout.egym.exercise.list.interaction

import com.ch4k4uw.workout.egym.core.exercise.domain.data.ExerciseTag
import com.ch4k4uw.workout.egym.login.interaction.UserView
import java.io.Serializable

sealed class ExerciseListState : Serializable {
    data class DisplayUserData(val user: UserView) : ExerciseListState()
    object ShowLoginScreen : ExerciseListState()
    object ExerciseListTag : ExerciseListState()
    data class ShowExerciseList(val exercises: List<ExerciseHeadView>) : ExerciseListState()
    object ShowNoMorePagesToFetch : ExerciseListState()
    data class ShowExerciseTagList(val tags: List<ExerciseTag>) : ExerciseListState()
}
