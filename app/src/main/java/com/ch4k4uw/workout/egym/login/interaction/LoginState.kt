package com.ch4k4uw.workout.egym.login.interaction

import android.content.Intent

sealed class LoginState {
    data class PerformGoogleSignIn(val intent: Intent) : LoginState()
    data class ShowSignedInUser(val user: UserView) : LoginState()
}
