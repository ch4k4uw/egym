package com.ch4k4uw.workout.egym.login.interaction

import android.content.Intent
import java.io.Serializable

sealed class LoginState : Serializable {
    data class PerformGoogleSignIn(val intent: Intent) : LoginState()
    data class ShowSignedInUser(val user: UserView) : LoginState()
}
