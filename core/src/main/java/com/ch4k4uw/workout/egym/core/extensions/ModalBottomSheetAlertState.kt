package com.ch4k4uw.workout.egym.core.extensions

import android.content.Context
import com.ch4k4uw.workout.egym.core.R
import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlertState

suspend fun ModalBottomSheetAlertState.showGenericErrorAlert(
    context: Context,
    callId: Int
) {
    show(
        callId = callId,
        type = ModalBottomSheetAlertState.ModalType.Error,
        title = context.getString(R.string.generic_error_title),
        message = context.getString(R.string.generic_error_message),
        positiveButtonLabel = context.getString(R.string.generic_error_positive_button),
        negativeButtonLabel = context.getString(R.string.generic_error_negative_button),
    )
}

suspend fun ModalBottomSheetAlertState.showConnectivityErrorAlert(
    context: Context,
    callId: Int
) {
    show(
        callId = callId,
        type = ModalBottomSheetAlertState.ModalType.Warning,
        title = context.getString(R.string.connectivity_error_title),
        message = context.getString(R.string.connectivity_error_message),
        positiveButtonLabel = context.getString(R.string.connectivity_error_positive_button),
        negativeButtonLabel = context.getString(R.string.connectivity_error_negative_button),
    )
}