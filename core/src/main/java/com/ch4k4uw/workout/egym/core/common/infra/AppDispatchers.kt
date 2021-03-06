package com.ch4k4uw.workout.egym.core.common.infra

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDispatchers @Inject constructor() {
    val io = Dispatchers.IO
    val main = Dispatchers.Main
}