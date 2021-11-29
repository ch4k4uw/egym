package com.ch4k4uw.workout.egym.login.domain.interactor

import android.content.Intent
import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.core.auth.domain.repository.UserRepository
import com.ch4k4uw.workout.egym.core.auth.infra.injection.FirebaseSubComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ViewModelScoped
class LoginInteractor @Inject constructor(
    fbSubComponentFactory: FirebaseSubComponent.Factory,
    private val userRepository: UserRepository
) {
    private val fbSubComponent = fbSubComponentFactory
        .create()

    fun findGoogleFbSignInIntent(): Flow<Intent> =
        flowOf(fbSubComponent.googleSignInIntent)

    fun parseGoogleFbSignInResult(intent: Intent): Flow<User> =
        fbSubComponent.parseGoogleFirebaseSignInResult.parse(intent = intent)

    suspend fun findLoggedUser(): Flow<User> =
        userRepository.findLoggedUser()
}