package com.ch4k4uw.workout.egym.login.interaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserView(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val image: String = ""
) : Parcelable {
    companion object {
        val Empty = UserView()
    }
}
