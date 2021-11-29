package com.ch4k4uw.workout.egym.core.common.infra.injection

import android.content.Context
import com.ch4k4uw.workout.egym.core.common.infra.data.HttpLoggerFactory
import com.ch4k4uw.workout.egym.core.common.infra.data.NetworkStatus
import com.ch4k4uw.workout.egym.core.common.infra.data.NetworkStatusImpl
import com.ch4k4uw.workout.egym.core.common.infra.data.factory.HttpClientFactory
import com.ch4k4uw.workout.egym.core.common.infra.data.factory.HttpClientFactoryImpl
import com.ch4k4uw.workout.egym.core.common.infra.data.factory.HttpServiceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideHttpLoggerFactory(): HttpLoggerFactory =
        HttpLoggerFactory()

    @Provides
    fun provideNetworkStatus(@ApplicationContext context: Context): NetworkStatus =
        NetworkStatusImpl(context = context)

    @Provides
    @Singleton
    fun provideHttpClientFactory(
        networkStatus: NetworkStatus,
        httpLoggerFactory: HttpLoggerFactory,
    ): HttpClientFactory = HttpClientFactoryImpl(
        networkStatus = networkStatus, loggerFactory = httpLoggerFactory
    )

    @Provides
    @Singleton
    fun provideHttpServiceFactory(
        httpClientFactory: HttpClientFactory
    ) = HttpServiceFactory(httpClientFactory = httpClientFactory)

}