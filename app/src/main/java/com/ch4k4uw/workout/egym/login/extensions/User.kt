package com.ch4k4uw.workout.egym.login.extensions

import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.login.interaction.UserView

fun User.toView(): UserView =
    UserView(
        id = id,
        name = name,
        email = email,
        image = image
    )