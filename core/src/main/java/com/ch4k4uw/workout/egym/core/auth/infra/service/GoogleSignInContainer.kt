package com.ch4k4uw.workout.egym.core.auth.infra.service

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth


class GoogleSignInContainer (
    context: Context,
    webClientId: String
) {
    private val googleSignInClient by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
            .let {
                GoogleSignIn.getClient(context, it)
            }
    }

    internal val fbAuth by lazy {
        FirebaseAuth.getInstance()
    }

    internal val signInIntent by lazy {
        googleSignInClient.signInIntent
    }
}