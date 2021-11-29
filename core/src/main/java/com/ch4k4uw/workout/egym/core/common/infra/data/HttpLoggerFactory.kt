package com.ch4k4uw.workout.egym.core.common.infra.data

import com.ch4k4uw.workout.egym.core.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class HttpLoggerFactory internal constructor() {
    fun create(): Interceptor = object : Interceptor {
        private val interceptor = HttpLoggingInterceptor()
            .also {
                if (BuildConfig.DEBUG) {
                    it.level = HttpLoggingInterceptor.Level.BODY
                }
            }

        override fun intercept(chain: Interceptor.Chain): Response =
            interceptor.intercept(chain)
    }
}