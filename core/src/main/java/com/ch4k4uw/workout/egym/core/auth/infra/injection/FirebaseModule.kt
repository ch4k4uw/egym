package com.ch4k4uw.workout.egym.core.auth.infra.injection

import android.content.Context
import com.ch4k4uw.workout.egym.core.auth.domain.service.ParseGoogleFirebaseSignInResultService
import com.ch4k4uw.workout.egym.core.auth.infra.injection.qualifier.GoogleSignInIntent
import com.ch4k4uw.workout.egym.core.auth.infra.injection.qualifier.WebClientId
import com.ch4k4uw.workout.egym.core.auth.infra.service.GoogleSignInContainer
import com.ch4k4uw.workout.egym.core.auth.infra.service.ParseGoogleFirebaseSignInResultServiceImpl
import com.ch4k4uw.workout.egym.core.common.infra.AppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class FirebaseModule {
    @Provides
    @ActivityScoped
    fun provideGoogleSignInContainer(
        @ApplicationContext context: Context,
        @WebClientId webClientId: String
    ): GoogleSignInContainer =
        GoogleSignInContainer(
            context = context,
            webClientId = webClientId
        )

    @Provides
    @ActivityScoped
    fun provideParseGoogleFirebaseSignInResultService(
        googleSignInContainer: GoogleSignInContainer,
        appDispatchers: AppDispatchers
    ): ParseGoogleFirebaseSignInResultService =
        ParseGoogleFirebaseSignInResultServiceImpl(
            fbAuth = googleSignInContainer.fbAuth,
            dispatchers = appDispatchers
        )

    @Provides
    @ActivityScoped
    @GoogleSignInIntent
    fun provideGoogleSignInIntent(googleSignInContainer: GoogleSignInContainer) =
        googleSignInContainer.signInIntent

}