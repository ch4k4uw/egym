package com.ch4k4uw.workout.egym.common.domain

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.util.Base64
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DecodeFromRouteService @Inject constructor() {
    inline fun <reified T: Parcelable> decode(data: String): T {
        val creator = T::class.java
            .fields
            .find { it.name == "CREATOR" }
            ?.get(null)
            ?.let { it as? Parcelable.Creator<*> }
            ?: throw RuntimeException("${T::class.java.name}::CREATOR not found")

        val rawData = Uri.decode(data).let { Base64.decode(it, Base64.NO_WRAP) }
        val parcel = Parcel.obtain()
        parcel.unmarshall(rawData, 0, rawData.size)
        parcel.setDataPosition(0)
        val result = creator.createFromParcel(parcel)
        parcel.recycle()
        return result as T
    }
}