package com.ch4k4uw.workout.egym.core.common.infra.service

import com.ch4k4uw.workout.egym.core.BuildConfig
import com.ch4k4uw.workout.egym.core.exercise.infra.data.ExerciseHeadPagerRemote
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface HelperApi {
    @GET("exercise/head")
    @Headers("authorization: ${BuildConfig.API_AUTH}")
    suspend fun findExerciseHeadPager(
        @Query("sz") pageSize: Int,
        @Query("qs") queryString: String? = null,
        @Query("tg") queryTags: List<String>? = null
    ): ExerciseHeadPagerRemote

    @GET("exercise/head")
    suspend fun findNextExerciseHeadPage(
        @Query("sz") pageSize: Int,
        @Query("cc") collectionCount: Int,
        @Query("cr") pageIndex: Int,
        @Query("ls") lastItemTitle: String,
        @Query("ld") lastItemTime: Long,
        @Query("pc") pageCount: Int,
        @Query("op") operation: String = "next",
        @Query("qs") queryString: String? = null,
        @Query("tg") queryTags: List<String>? = null
    ): ExerciseHeadPagerRemote
}