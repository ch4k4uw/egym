package com.ch4k4uw.workout.egym.injection

import com.ch4k4uw.workout.egym.common.domain.DecodeFromRouteService
import com.ch4k4uw.workout.egym.common.domain.EncodeToRouteService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface RouteEncodeEntryPoint {
    val decodeFromRoute: DecodeFromRouteService
    val encodeToRoute: EncodeToRouteService
}