@file:SuppressWarnings("unchecked")

package com.ch4k4uw.workout.egym.extensions

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import android.util.Base64
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

inline fun <reified T : Parcelable> String.toParcelable(): T {
    val creator = T::class.java
        .fields
        .find { it.name == "CREATOR" }
        ?.get(null)
        ?.let { it as? Parcelable.Creator<*> }
        ?: throw RuntimeException("${T::class.java.name}::CREATOR not found")

    val rawData = Uri.decode(this).let { Base64.decode(it, Base64.NO_WRAP) }
    val parcel = Parcel.obtain()
    parcel.unmarshall(rawData, 0, rawData.size)
    parcel.setDataPosition(0)
    val result = creator.createFromParcel(parcel)
    parcel.recycle()
    return result as T
}

fun String.toTextFieldValue(
    selection: TextRange = TextRange(index = this.length)
): TextFieldValue = TextFieldValue(text = this, selection = selection)