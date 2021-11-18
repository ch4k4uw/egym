package com.ch4k4uw.workout.egym.core.auth.infra.injection

import java.lang.annotation.ElementType
import java.lang.annotation.Target
import javax.inject.Qualifier

@Qualifier
@kotlin.annotation.Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER
)
annotation class WebClientId
