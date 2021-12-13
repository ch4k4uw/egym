package com.ch4k4uw.workout.egym.core.ui.components.interaction

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

@ExperimentalMaterialApi
data class ModalBottomSheetAlertInteraction(
    val state: ModalBottomSheetAlertState,
    val result: State<ModalBottomSheetAlertResultState>
)

@Composable
fun rememberModalBottomSheetAlertInteraction(
    state: ModalBottomSheetAlertState,
    result: State<ModalBottomSheetAlertResultState>
) = remember {
    ModalBottomSheetAlertInteraction(state = state, result = result)
}

@ExperimentalMaterialApi
val LocalModalBottomSheetAlertInteraction =
    staticCompositionLocalOf<ModalBottomSheetAlertInteraction> {
        TODO("Undefined")
    }
