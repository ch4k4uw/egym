package com.ch4k4uw.workout.egym.core.extensions

import java.time.LocalDateTime
import java.time.ZoneId

val LocalDateTime.asMilliseconds: Long
    get() = atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()