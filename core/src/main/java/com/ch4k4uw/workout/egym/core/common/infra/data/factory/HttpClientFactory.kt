package com.ch4k4uw.workout.egym.core.common.infra.data.factory

import okhttp3.OkHttpClient

interface HttpClientFactory {
    fun create(): OkHttpClient
}