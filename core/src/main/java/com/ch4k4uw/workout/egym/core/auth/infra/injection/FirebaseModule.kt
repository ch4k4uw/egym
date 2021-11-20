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
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.FragmentScoped

@Module
@InstallIn(FragmentComponent::class)
class FirebaseModule {
    @Provides
    @FragmentScoped
    fun provideGoogleSignInContainer(
        @ApplicationContext context: Context,
        @WebClientId webClientId: String
    ): GoogleSignInContainer =
        GoogleSignInContainer(
            context = context,
            webClientId = webClientId
        )

    @Provides
    @FragmentScoped
    fun provideParseGoogleFirebaseSignInResultService(
        googleSignInContainer: GoogleSignInContainer,
        appDispatchers: AppDispatchers
    ): ParseGoogleFirebaseSignInResultService =
        ParseGoogleFirebaseSignInResultServiceImpl(
            fbAuth = googleSignInContainer.fbAuth,
            dispatchers = appDispatchers
        )

    @Provides
    @FragmentScoped
    @GoogleSignInIntent
    fun provideGoogleSignInIntent(googleSignInContainer: GoogleSignInContainer) =
        googleSignInContainer.signInIntent

}