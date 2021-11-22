package com.ch4k4uw.workout.egym.core.common.infra.data

import com.ch4k4uw.workout.egym.core.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

internal class HttpLogger @Inject constructor() : Interceptor {
    private val interceptor = HttpLoggingInterceptor()
        .also {
            if (BuildConfig.DEBUG) {
                it.level = HttpLoggingInterceptor.Level.BODY
            }
        }

    override fun intercept(chain: Interceptor.Chain): Response =
        interceptor.intercept(chain)
}