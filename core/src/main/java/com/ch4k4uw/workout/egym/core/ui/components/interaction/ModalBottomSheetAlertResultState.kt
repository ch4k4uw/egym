package com.ch4k4uw.workout.egym.core.ui.components.interaction

import java.io.Serializable
import java.util.*

sealed interface ModalBottomSheetAlertResultState : Serializable {
    val uuid: String

    data class Idle(override val uuid: String = UUID.randomUUID().toString()) :
        ModalBottomSheetAlertResultState

    sealed interface ClickedState : ModalBottomSheetAlertResultState {
        val callId: Int
    }

    data class PositiveClicked(
        override val callId: Int, override val uuid: String = UUID.randomUUID().toString()
    ) : ModalBottomSheetAlertResultState, ClickedState

    data class NegativeClicked(
        override val callId: Int, override val uuid: String = UUID.randomUUID().toString()
    ) : ModalBottomSheetAlertResultState, ClickedState
}
