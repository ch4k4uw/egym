package com.ch4k4uw.workout.egym.core.ui.components.interaction

import android.os.Bundle
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable

@Stable
@ExperimentalMaterialApi
class ModalBottomSheetAlertState(
    val modalState: ModalBottomSheetState
) {
    enum class ModalType {
        Error, Warning, Info, Question
    }

    private val mutableCallId = mutableStateOf(0)
    private val mutableType = mutableStateOf(ModalType.Info)
    private val mutableTitle = mutableStateOf("")
    private val mutableMessage = mutableStateOf("")
    private val mutablePositiveButtonLabel = mutableStateOf(null as String?)
    private val mutableNegativeButtonLabel = mutableStateOf(null as String?)

    val callId: State<Int> = mutableCallId
    val type: State<ModalType> = mutableType
    val title: State<String> = mutableTitle
    val message: State<String> = mutableMessage
    val positiveButtonLabel: State<String?> = mutablePositiveButtonLabel
    val negativeButtonLabel: State<String?> = mutableNegativeButtonLabel

    suspend fun show(
        callId: Int,
        type: ModalType,
        title: String,
        message: String,
        positiveButtonLabel: String? = null,
        negativeButtonLabel: String? = null,
    ) {
        mutableCallId.value = callId
        mutableType.value = type
        mutableTitle.value = title
        mutableMessage.value = message
        mutablePositiveButtonLabel.value = positiveButtonLabel
        mutableNegativeButtonLabel.value = negativeButtonLabel
        modalState.show()
    }

    suspend fun hide() {
        modalState.hide()
    }

    companion object {
        fun Saver(
            modalState: ModalBottomSheetState
        ): Saver<ModalBottomSheetAlertState, *> = Saver(
            save = {
                Bundle().apply {
                    putInt("cid", it.mutableCallId.value)
                    putInt("tp", it.mutableType.value.ordinal)
                    putString("tt", it.mutableTitle.value)
                    putString("ds", it.mutableMessage.value)
                    putString("pb", it.mutablePositiveButtonLabel.value)
                    putString("nb", it.mutableNegativeButtonLabel.value)
                }
            },
            restore = {
                ModalBottomSheetAlertState(modalState = modalState).apply {
                    mutableCallId.value = it.getInt("cid", 0)
                    mutableType.value = it.getInt("tp", 0).let(ModalType.values()::get)
                    mutableTitle.value = it.getString("tt").orEmpty()
                    mutableMessage.value = it.getString("ds").orEmpty()
                    mutablePositiveButtonLabel.value = it.getString("pb")
                    mutableNegativeButtonLabel.value = it.getString("nb")
                }
            }
        )
    }
}

@ExperimentalMaterialApi
@Composable
fun rememberModalBottomSheetAlertState(): ModalBottomSheetAlertState {
    val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    return rememberSaveable(saver = ModalBottomSheetAlertState.Saver(modalState = modalState)) {
        ModalBottomSheetAlertState(modalState = modalState)
    }
}