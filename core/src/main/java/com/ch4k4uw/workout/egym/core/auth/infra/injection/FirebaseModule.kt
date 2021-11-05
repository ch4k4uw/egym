package com.ch4k4uw.workout.egym.core.auth.infra.injection

import android.content.Context
import com.ch4k4uw.workout.egym.core.BuildConfig
import com.ch4k4uw.workout.egym.core.auth.domain.service.ParseGoogleFirebaseSignInResultService
import com.ch4k4uw.workout.egym.core.auth.infra.service.ParseGoogleFirebaseSignInResultServiceImpl
import com.ch4k4uw.workout.egym.core.common.infra.AppDispatchers
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ActivityComponent::class)
class FirebaseModule {
    @Provides
    fun provideSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.DEFAULT_WEB_CLIENT_ID)
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    fun provideParseGoogleFirebaseSignInResultService(
        fbAuth: FirebaseAuth,
        appDispatchers: AppDispatchers
    ): ParseGoogleFirebaseSignInResultService =
        ParseGoogleFirebaseSignInResultServiceImpl(
            fbAuth = fbAuth,
            dispatchers = appDispatchers
        )

}