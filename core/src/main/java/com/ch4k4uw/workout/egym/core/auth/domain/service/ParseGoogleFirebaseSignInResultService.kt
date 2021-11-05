package com.ch4k4uw.workout.egym.core.auth.domain.service

import android.content.Intent
import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import kotlinx.coroutines.flow.Flow

interface ParseGoogleFirebaseSignInResultService {
    fun parse(intent: Intent): Flow<User>
}