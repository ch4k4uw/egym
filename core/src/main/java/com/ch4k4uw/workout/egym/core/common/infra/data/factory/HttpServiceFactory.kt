package com.ch4k4uw.workout.egym.core.common.infra.data.factory

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

class HttpServiceFactory internal constructor(
    private val httpClientFactory: HttpClientFactory
) {
    fun <T : Any> createService(
        serviceClass: KClass<T>,
        baseUrl: String
    ): T = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .client(httpClientFactory.create())
        .build().create(serviceClass.java)
}