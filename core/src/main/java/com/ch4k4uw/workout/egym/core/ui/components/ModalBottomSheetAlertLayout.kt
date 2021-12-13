package com.ch4k4uw.workout.egym.core.ui.components

import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlertResultState
import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlertState
import com.ch4k4uw.workout.egym.core.ui.components.interaction.rememberModalBottomSheetAlertState
import com.google.accompanist.insets.navigationBarsPadding
import org.jetbrains.annotations.TestOnly


private val InteractionSaver: Saver<MutableState<ModalBottomSheetAlertResultState>, *> = Saver(
    save = { Bundle().apply { putSerializable("stt", it.value) } },
    restore = { data ->
        data.getSerializable("stt")
            ?.let { mutableStateOf(it as ModalBottomSheetAlertResultState) }
    }
)

//region main
@ExperimentalUnitApi
@ExperimentalMaterialApi
@Composable
fun ModalBottomSheetAlertLayout(
    state: ModalBottomSheetAlertState = rememberModalBottomSheetAlertState(),
    content: @Composable (State<ModalBottomSheetAlertResultState>) -> Unit
) {
    val interactionState = rememberSaveable(saver = InteractionSaver) {
        mutableStateOf(ModalBottomSheetAlertResultState.Idle())
    }

    ModalBottomSheetLayout(
        sheetState = state.modalState,
        sheetContent = {
            SheetContent(
                type = state.type.value,
                title = state.title.value,
                message = state.message.value,
                onPositiveClicked = {
                    interactionState.value = ModalBottomSheetAlertResultState.PositiveClicked(
                        callId = state.callId.value
                    )
                },
                positiveButtonLabel = state.positiveButtonLabel.value,
                onNegativeClicked = {
                    interactionState.value = ModalBottomSheetAlertResultState.NegativeClicked(
                        callId = state.callId.value
                    )
                },
                negativeButtonLabel = state.negativeButtonLabel.value,
            )
        },
        sheetShape = RoundedCornerShape(
            topStart = AppTheme.Dimens.sizing.small,
            topEnd = AppTheme.Dimens.sizing.small,
        ),
        content = { content(interactionState) }
    )

    key(state.callId.value) {
        interactionState.value = ModalBottomSheetAlertResultState.Idle()
    }
}
//endregion

//region body
@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
private fun SheetContent(
    type: ModalBottomSheetAlertState.ModalType,
    title: String,
    message: String,
    onPositiveClicked: (() -> Unit)? = null,
    positiveButtonLabel: String? = null,
    onNegativeClicked: (() -> Unit)? = null,
    negativeButtonLabel: String? = null
) {
    val configs = object {
        val color = when (type) {
            ModalBottomSheetAlertState.ModalType.Warning -> AppTheme.colors.amber
            ModalBottomSheetAlertState.ModalType.Error -> AppTheme.colors.deepOrange
            ModalBottomSheetAlertState.ModalType.Info -> AppTheme.colors.green
            ModalBottomSheetAlertState.ModalType.Question -> AppTheme.colors.green
        }
        val icon = when (type) {
            ModalBottomSheetAlertState.ModalType.Warning -> Icons.Filled.Warning
            ModalBottomSheetAlertState.ModalType.Error -> Icons.Filled.Close
            ModalBottomSheetAlertState.ModalType.Info -> Icons.Filled.Info
            ModalBottomSheetAlertState.ModalType.Question -> Icons.Filled.Info
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xxnormal))
        Icon(
            imageVector = configs.icon,
            contentDescription = null,
            modifier = Modifier
                .requiredSize(size = AppTheme.Dimens.sizing.xnormal)
        )
        Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xnormal))
        Text(text = title, style = AppTheme.typography.material.h5)
        Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.normal))
        Surface(
            color = if (AppTheme.colors.material.isLight) {
                Color.DarkGray.copy(alpha = .1f)
            } else {
                Color.LightGray.copy(alpha = .1f)
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.Dimens.spacing.normal),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(
                    modifier = Modifier
                        .clip(shape = AppTheme.shapes.material.medium)
                        .fillMaxWidth()
                        .height(height = AppTheme.Dimens.spacing.tiny)
                        .background(color = configs.color)
                )
                Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xxnormal))
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    style = AppTheme.typography.material.subtitle1
                )
                Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.xxnormal))
                if (positiveButtonLabel != null) {
                    Button(
                        onClick = { onPositiveClicked?.invoke() },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = AppTheme.colors.material.secondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(size = AppTheme.Dimens.sizing.xsmall)
                    ) {
                        Text(text = positiveButtonLabel)
                    }
                }
                if (negativeButtonLabel != null) {
                    if (positiveButtonLabel != null) {
                        Spacer(modifier = Modifier.height(height = AppTheme.Dimens.spacing.normal))
                    }
                    OutlinedButton(
                        onClick = { onNegativeClicked?.invoke() },
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = AppTheme.colors.material.surface.copy(
                                alpha = 0f
                            )
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(size = AppTheme.Dimens.sizing.xsmall)
                    ) {
                        Text(text = negativeButtonLabel)
                    }
                }
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(height = AppTheme.Dimens.spacing.xxnormal)
                )
            }
        }
    }
}
//endregion

//region Preview
@TestOnly
@ExperimentalMaterialApi
private val modalType = ModalBottomSheetAlertState.ModalType.Error

@ExperimentalUnitApi
@ExperimentalMaterialApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Dark", name = "No buttons")
@Composable
private fun PreviewDarkNoButtons() {
    AppTheme {
        Box(modifier = Modifier.background(color = AppTheme.colors.material.surface)) {
            CompositionLocalProvider(
                LocalContentColor provides AppTheme.colors.material.onSurface
            ) {
                SheetContent(
                    type = modalType,
                    title = "Connectivity Error",
                    message = "Connectivity problems with the server. Try again later."
                )
            }
        }
    }
}

@ExperimentalUnitApi
@ExperimentalMaterialApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Dark", name = "Positive button")
@Composable
private fun PreviewDarkPositiveButton() {
    AppTheme {
        Box(modifier = Modifier.background(color = AppTheme.colors.material.surface)) {
            CompositionLocalProvider(
                LocalContentColor provides AppTheme.colors.material.onSurface
            ) {
                SheetContent(
                    type = modalType,
                    title = "Connectivity Error",
                    message = "Connectivity problems with the server. Try again later.",
                    positiveButtonLabel = "Ok"
                )
            }
        }
    }
}

@ExperimentalUnitApi
@ExperimentalMaterialApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Dark", name = "Positive button")
@Composable
private fun PreviewDarkNegativeButton() {
    AppTheme {
        Box(modifier = Modifier.background(color = AppTheme.colors.material.surface)) {
            CompositionLocalProvider(
                LocalContentColor provides AppTheme.colors.material.onSurface
            ) {
                SheetContent(
                    type = modalType,
                    title = "Connectivity Error",
                    message = "Connectivity problems with the server. Try again later.",
                    negativeButtonLabel = "Cancel"
                )
            }
        }
    }
}

@ExperimentalUnitApi
@ExperimentalMaterialApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, group = "Dark", name = "Positive button")
@Composable
private fun PreviewDarkAllButtons() {
    AppTheme {
        Box(modifier = Modifier.background(color = AppTheme.colors.material.surface)) {
            CompositionLocalProvider(
                LocalContentColor provides AppTheme.colors.material.onSurface
            ) {
                SheetContent(
                    type = modalType,
                    title = "Connectivity Error",
                    message = "Connectivity problems with the server. Try again later.",
                    positiveButtonLabel = "Try Again",
                    negativeButtonLabel = "Cancel"
                )
            }
        }
    }
}

@ExperimentalUnitApi
@ExperimentalMaterialApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, group = "Light", name = "No buttons")
@Composable
private fun PreviewLightNoButtons() {
    AppTheme {
        Box(modifier = Modifier.background(color = AppTheme.colors.material.surface)) {
            CompositionLocalProvider(
                LocalContentColor provides AppTheme.colors.material.onSurface
            ) {
                SheetContent(
                    type = modalType,
                    title = "Connectivity Error",
                    message = "Connectivity problems with the server. Try again later."
                )
            }
        }
    }
}

@ExperimentalUnitApi
@ExperimentalMaterialApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, group = "Light", name = "Positive button")
@Composable
private fun PreviewLightPositiveButton() {
    AppTheme {
        Box(modifier = Modifier.background(color = AppTheme.colors.material.surface)) {
            CompositionLocalProvider(
                LocalContentColor provides AppTheme.colors.material.onSurface
            ) {
                SheetContent(
                    type = modalType,
                    title = "Connectivity Error",
                    message = "Connectivity problems with the server. Try again later.",
                    positiveButtonLabel = "Ok"
                )
            }
        }
    }
}

@ExperimentalUnitApi
@ExperimentalMaterialApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, group = "Light", name = "Positive button")
@Composable
private fun PreviewLightNegativeButton() {
    AppTheme {
        Box(modifier = Modifier.background(color = AppTheme.colors.material.surface)) {
            CompositionLocalProvider(
                LocalContentColor provides AppTheme.colors.material.onSurface
            ) {
                SheetContent(
                    type = modalType,
                    title = "Connectivity Error",
                    message = "Connectivity problems with the server. Try again later.",
                    negativeButtonLabel = "Cancel"
                )
            }
        }
    }
}

@ExperimentalUnitApi
@ExperimentalMaterialApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, group = "Light", name = "Positive button")
@Composable
private fun PreviewLightAllButtons() {
    AppTheme {
        Box(modifier = Modifier.background(color = AppTheme.colors.material.surface)) {
            CompositionLocalProvider(
                LocalContentColor provides AppTheme.colors.material.onSurface
            ) {
                SheetContent(
                    type = modalType,
                    title = "Connectivity Error",
                    message = "Connectivity problems with the server. Try again later.",
                    positiveButtonLabel = "Try Again",
                    negativeButtonLabel = "Cancel"
                )
            }
        }
    }
}
//endregion