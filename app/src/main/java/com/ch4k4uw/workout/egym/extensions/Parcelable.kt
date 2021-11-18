package com.ch4k4uw.workout.egym.extensions

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.util.Base64

val Parcelable.asUriString: String
    get() {
        val parcel = Parcel.obtain()
        writeToParcel(parcel, 0)
        val result = parcel.marshall().let {
            Base64.encodeToString(it, Base64.NO_WRAP)
        }.let {
            Uri.encode(it)
        }
        parcel.recycle()
        return result
    }