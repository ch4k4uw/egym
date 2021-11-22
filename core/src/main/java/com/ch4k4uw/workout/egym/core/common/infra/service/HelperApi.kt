package com.ch4k4uw.workout.egym.core.common.infra.service

import com.ch4k4uw.workout.egym.core.common.infra.data.CollectionCountRemote
import retrofit2.http.GET
import retrofit2.http.Path

interface HelperApi {
    @GET("{collection}/count")
    suspend fun findCollectionCount(
        @Path("collection") collection: String
    ): CollectionCountRemote
}