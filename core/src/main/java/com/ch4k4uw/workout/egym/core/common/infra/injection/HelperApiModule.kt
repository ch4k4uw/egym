package com.ch4k4uw.workout.egym.core.common.infra.injection

import com.ch4k4uw.workout.egym.core.BuildConfig
import com.ch4k4uw.workout.egym.core.common.infra.data.factory.HttpServiceFactory
import com.ch4k4uw.workout.egym.core.common.infra.service.HelperApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class HelperApiModule {
    @Provides
    fun providesHelperApi(serviceFactory: HttpServiceFactory): HelperApi =
        serviceFactory.createService(
            serviceClass = HelperApi::class,
            baseUrl = BuildConfig.HELPER_API_URL
        )
}