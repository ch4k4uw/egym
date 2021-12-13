package com.ch4k4uw.workout.egym.core.ui.components.interaction

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import com.ch4k4uw.workout.egym.core.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
sealed interface ModalBottomSheetAlert {
    val result: State<ModalBottomSheetAlertResultState>
    fun showGenericErrorAlert(callId: Int)
    fun showConnectivityErrorAlert(callId: Int)
    fun showAlert(
        callId: Int,
        title: String,
        message: String,
        type: ModalBottomSheetAlertState.ModalType,
        positiveButtonLabel: String? = null,
        negativeButtonLabel: String? = null,
    )
    fun hide()
}

private class ModalBottomSheetAlertImpl(
    private val context: Context,
    private val interaction: ModalBottomSheetAlertInteraction,
    private val scope: CoroutineScope
) : ModalBottomSheetAlert {
    override val result: State<ModalBottomSheetAlertResultState> = interaction.result

    override fun showGenericErrorAlert(callId: Int) {
        scope.launch {
            showAlert(
                callId = callId,
                type = ModalBottomSheetAlertState.ModalType.Error,
                title = context.getString(R.string.generic_error_title),
                message = context.getString(R.string.generic_error_message),
                positiveButtonLabel = context.getString(R.string.generic_error_positive_button),
                negativeButtonLabel = context.getString(R.string.generic_error_negative_button),
            )
        }
    }

    override fun showConnectivityErrorAlert(callId: Int) {
        scope.launch {
            showAlert(
                callId = callId,
                type = ModalBottomSheetAlertState.ModalType.Warning,
                title = context.getString(R.string.connectivity_error_title),
                message = context.getString(R.string.connectivity_error_message),
                positiveButtonLabel = context.getString(R.string.connectivity_error_positive_button),
                negativeButtonLabel = context.getString(R.string.connectivity_error_negative_button),
            )
        }
    }

    override fun showAlert(
        callId: Int,
        title: String,
        message: String,
        type: ModalBottomSheetAlertState.ModalType,
        positiveButtonLabel: String?,
        negativeButtonLabel: String?,
    ) {
        scope.launch {
            interaction
                .state
                .show(
                    callId = callId,
                    title = title,
                    message = message,
                    type = type,
                    positiveButtonLabel = positiveButtonLabel,
                    negativeButtonLabel = negativeButtonLabel
                )
        }
    }

    override fun hide() {
        scope.launch {
            interaction.state.hide()
        }
    }
}

@Composable
fun rememberModalBottomSheetAlert(
    context: Context = LocalContext.current,
    interaction: ModalBottomSheetAlertInteraction = LocalModalBottomSheetAlertInteraction.current,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): ModalBottomSheetAlert = remember(key1 = context) {
    ModalBottomSheetAlertImpl(
        context = context,
        interaction = interaction,
        scope = coroutineScope
    )
}

@Composable
@NonRestartableComposable
fun ModalBottomSheetAlertEffect(
    modalAlert: ModalBottomSheetAlert,
    block: ModalBottomSheetAlert.() -> Unit
) {
    remember(key1 = modalAlert, key2 = modalAlert.result.value) {
        object : RememberObserver {
            private var mModalAlert: ModalBottomSheetAlert? = modalAlert
            private var called = false
            override fun onAbandoned() {
            }
            override fun onForgotten() {
                mModalAlert = null
            }
            override fun onRemembered() {
                if(!called) {
                    called = true
                    mModalAlert?.apply {
                        block()
                    }
                }
            }
        }
    }
}