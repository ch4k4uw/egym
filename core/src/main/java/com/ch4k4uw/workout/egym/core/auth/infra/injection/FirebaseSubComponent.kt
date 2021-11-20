package com.ch4k4uw.workout.egym.core.auth.infra.injection

import android.content.Intent
import com.ch4k4uw.workout.egym.core.auth.domain.service.ParseGoogleFirebaseSignInResultService
import com.ch4k4uw.workout.egym.core.auth.infra.injection.qualifier.GoogleSignInIntent
import com.ch4k4uw.workout.egym.core.auth.infra.repository.UserCmdRepositoryImpl
import com.ch4k4uw.workout.egym.core.auth.infra.service.GoogleSignInContainer
import dagger.Subcomponent
import dagger.hilt.android.scopes.FragmentScoped

@Subcomponent(
    modules = [
        FirebaseModule::class
    ]
)
@FragmentScoped
interface FirebaseSubComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): FirebaseSubComponent
    }

    @get:GoogleSignInIntent
    val googleSignInIntent: Intent

    val parseGoogleFirebaseSignInResult: ParseGoogleFirebaseSignInResultService

    var googleSignInContainer: GoogleSignInContainer
}