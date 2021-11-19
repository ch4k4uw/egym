package com.ch4k4uw.workout.egym.core.auth.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserCmdRepository : UserRepository {
    suspend fun performLogout(): Flow<Unit>
}