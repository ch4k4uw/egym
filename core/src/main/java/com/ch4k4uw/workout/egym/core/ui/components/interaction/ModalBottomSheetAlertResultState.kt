package com.ch4k4uw.workout.egym.core.ui.components.interaction

import java.io.Serializable
import java.util.*

sealed class ModalBottomSheetAlertResultState(open val uuid: String) : Serializable {
    data class Idle(override val uuid: String = UUID.randomUUID().toString()) :
        ModalBottomSheetAlertResultState(uuid)

    data class PositiveClicked(
        val callId: Int, override val uuid: String = UUID.randomUUID().toString()
    ) : ModalBottomSheetAlertResultState(uuid)

    data class NegativeClicked(
        val callId: Int, override val uuid: String = UUID.randomUUID().toString()
    ) : ModalBottomSheetAlertResultState(uuid)
}
