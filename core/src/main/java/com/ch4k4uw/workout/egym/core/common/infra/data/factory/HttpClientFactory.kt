package com.ch4k4uw.workout.egym.core.common.infra.data.factory

import com.ch4k4uw.workout.egym.core.common.domain.data.NoConnectivityException
import com.ch4k4uw.workout.egym.core.common.infra.data.HttpLogger
import com.ch4k4uw.workout.egym.core.common.infra.data.NetworkStatus
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
internal class HttpClientFactory @Inject constructor(
    private val networkStatus: NetworkStatus,
    private val loggerInterceptorProvider: Provider<HttpLogger>
) {
    fun create(): OkHttpClient = OkHttpClient.Builder().let { builder ->
        builder.addNetworkInterceptor(loggerInterceptorProvider.get())
        builder.addNetworkInterceptor {
            if (!networkStatus.hasInternetConnection) throw NoConnectivityException()
            it.proceed(it.request())
        }
        builder
            .readTimeout(180L, TimeUnit.SECONDS)
            .writeTimeout(180L, TimeUnit.SECONDS)
            .connectTimeout(180L, TimeUnit.SECONDS)
        builder.build()
    }

}