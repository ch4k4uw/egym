package com.ch4k4uw.workout.egym.core.auth.domain.repository

import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun findLoggedUser(): Flow<User>
}