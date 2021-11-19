package com.ch4k4uw.workout.egym.home.interaction

import com.ch4k4uw.workout.egym.login.interaction.UserView

sealed class HomeState {
    data class DisplayUserData(val user: UserView): HomeState()
    object ShowLoginScreen : HomeState()
}
