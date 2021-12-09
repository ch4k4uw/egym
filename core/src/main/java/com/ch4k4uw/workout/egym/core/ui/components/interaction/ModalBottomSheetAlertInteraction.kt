package com.ch4k4uw.workout.egym.core.ui.components.interaction

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.State
import androidx.compose.runtime.staticCompositionLocalOf

@ExperimentalMaterialApi
data class ModalBottomSheetAlertInteraction(
    val state: ModalBottomSheetAlertState,
    val interaction: State<ModalBottomSheetAlertResultState>
)

@ExperimentalMaterialApi
val LocalModalBottomSheetAlertInteraction =
    staticCompositionLocalOf<ModalBottomSheetAlertInteraction> {
        TODO("Undefined")
    }