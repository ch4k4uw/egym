package com.ch4k4uw.workout.egym.core.extensions

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

val Long?.asLocalDateTime: LocalDateTime
    get() = this?.run {
        Instant.ofEpochMilli(this).let { instant ->
            LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        }
    } ?: LocalDateTime.MIN