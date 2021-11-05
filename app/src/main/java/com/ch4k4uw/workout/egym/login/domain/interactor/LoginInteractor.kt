package com.ch4k4uw.workout.egym.login.domain.interactor

import android.content.Context
import android.content.Intent
import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.core.auth.infra.injection.FirebaseSubComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@ViewModelScoped
class LoginInteractor @Inject constructor(
    @ApplicationContext context: Context,
    fbSubComponentFactory: FirebaseSubComponent.Factory
) {
    private val fbSubComponent = fbSubComponentFactory
        .create(context = context)

    fun findGoogleFbSignInIntent(): Flow<Intent> =
        flowOf(fbSubComponent.googleSignInClient.signInIntent)

    fun parseGoogleFbSignInResult(intent: Intent): Flow<User> =
        fbSubComponent.parseGoogleFirebaseSignInResult.parse(intent = intent)
}