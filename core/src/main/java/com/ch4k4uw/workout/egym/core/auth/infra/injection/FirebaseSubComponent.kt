package com.ch4k4uw.workout.egym.core.auth.infra.injection

import android.content.Context
import com.ch4k4uw.workout.egym.core.auth.domain.service.ParseGoogleFirebaseSignInResultService
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(
    modules = [
        FirebaseModule::class
    ]
)
interface FirebaseSubComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ): FirebaseSubComponent
    }

    val context: Context

    val googleSignInClient: GoogleSignInClient

    val fbAuth: FirebaseAuth

    val parseGoogleFirebaseSignInResult: ParseGoogleFirebaseSignInResultService
}