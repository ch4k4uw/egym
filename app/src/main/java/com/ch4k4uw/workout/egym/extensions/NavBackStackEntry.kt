package com.ch4k4uw.workout.egym.extensions

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import com.ch4k4uw.workout.egym.common.domain.DecodeFromRouteService

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.viewModel(): T {
    val factory = HiltViewModelFactory(LocalContext.current, this)
    return androidx.lifecycle.viewmodel.compose.viewModel(
        key = T::class.java.simpleName, factory = factory
    )
}

inline fun <reified T : Parcelable> NavBackStackEntry.parseStringArgAsParcelable(
    key: String,
    decoder: DecodeFromRouteService
): Bundle? {
    return arguments
        ?.also { args ->
            val rawValue = args.getString(key)
            if (rawValue != null) {
                val value = decoder
                    .decode<T>(rawValue)
                args.remove(key)
                args.putParcelable(key, value)
            }
        }
}