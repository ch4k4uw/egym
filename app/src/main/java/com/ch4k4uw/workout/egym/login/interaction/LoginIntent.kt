package com.ch4k4uw.workout.egym.login.interaction

import android.content.Intent

sealed class LoginIntent {
    object PerformFirebaseGoogleSignIn : LoginIntent()
    data class ParseGoogleSignResult(val intent: Intent) : LoginIntent()
}
