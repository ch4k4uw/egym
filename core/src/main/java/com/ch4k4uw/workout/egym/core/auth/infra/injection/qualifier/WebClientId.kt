package com.ch4k4uw.workout.egym.core.auth.infra.injection.qualifier

import javax.inject.Qualifier

@Qualifier
@kotlin.annotation.Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER
)
annotation class WebClientId
