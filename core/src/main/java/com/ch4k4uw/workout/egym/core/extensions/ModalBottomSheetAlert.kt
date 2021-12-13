package com.ch4k4uw.workout.egym.core.extensions

import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlert
import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlertResultState

fun ModalBottomSheetAlert.asClickedState(
    vararg callId: Int,
    block: ModalBottomSheetAlertResultState.ClickedState.() -> Unit
) {
    result
        .value
        .let { it as? ModalBottomSheetAlertResultState.ClickedState }
        ?.apply {
            if(callId.any { it == this.callId }) {
                block()
            }
        }
}