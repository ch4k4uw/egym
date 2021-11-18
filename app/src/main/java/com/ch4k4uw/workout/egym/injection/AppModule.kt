package com.ch4k4uw.workout.egym.injection

import android.content.Context
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.core.auth.infra.injection.WebClientId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @WebClientId
    @Singleton
    fun provideWebClientId(@ApplicationContext context: Context): String =
        context.getString(R.string.default_web_client_id)
}