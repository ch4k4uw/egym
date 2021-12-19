package com.ch4k4uw.workout.egym.extensions

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import com.ch4k4uw.workout.egym.common.domain.DecodeFromRouteService

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry?.viewModel(): T? = this?.let {
    viewModel(viewModelStoreOwner = it)
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.viewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
): T {
    val factory = HiltViewModelFactory(LocalContext.current, this)
    return androidx.lifecycle.viewmodel.compose.viewModel(
        viewModelStoreOwner = viewModelStoreOwner, key = T::class.java.name, factory = factory
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