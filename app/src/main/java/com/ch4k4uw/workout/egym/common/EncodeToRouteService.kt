package com.ch4k4uw.workout.egym.common

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.util.Base64
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EncodeToRouteService @Inject constructor() {
    fun encode(entity: Parcelable): String {
        val parcel = Parcel.obtain()
        entity.writeToParcel(parcel, 0)
        val result = parcel.marshall().let {
            Base64.encodeToString(it, Base64.NO_WRAP)
        }.let {
            Uri.encode(it)
        }
        parcel.recycle()
        return result
    }
}