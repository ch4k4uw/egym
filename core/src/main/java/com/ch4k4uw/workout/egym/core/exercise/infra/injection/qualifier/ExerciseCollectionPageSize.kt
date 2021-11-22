package com.ch4k4uw.workout.egym.core.exercise.infra.injection.qualifier

import javax.inject.Qualifier

@Qualifier
@kotlin.annotation.Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY_GETTER,
)
annotation class ExerciseCollectionPageSize
