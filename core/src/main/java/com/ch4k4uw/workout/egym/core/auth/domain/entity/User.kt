package com.ch4k4uw.workout.egym.core.auth.domain.entity

data class User(
    val id: String,
    val name: String,
    val email: String,
    val image: String
) {
    companion object {
        val Empty = User(
            id = "",
            name = "",
            email = "",
            image = ""
        )
    }
}
