package com.ch4k4uw.workout.egym.login

import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.common.state.AppState
import com.ch4k4uw.workout.egym.core.extensions.asClickedState
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.core.ui.components.ContentLoadingProgressBar
import com.ch4k4uw.workout.egym.core.ui.components.SignInGoogleButton
import com.ch4k4uw.workout.egym.core.ui.components.SocialMediaButtonDefaults
import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlertEffect
import com.ch4k4uw.workout.egym.core.ui.components.interaction.ModalBottomSheetAlertResultState
import com.ch4k4uw.workout.egym.core.ui.components.interaction.rememberModalBottomSheetAlert
import com.ch4k4uw.workout.egym.login.interaction.LoginIntent
import com.ch4k4uw.workout.egym.login.interaction.LoginState
import com.ch4k4uw.workout.egym.login.interaction.UserView
import com.ch4k4uw.workout.egym.login.interaction.rememberBkgAnimation
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf

@ExperimentalUnitApi
@Composable
fun LoginScreen(
    uiState: Flow<AppState<LoginState>>,
    onIntent: (LoginIntent) -> Unit,
    onSuccessfulLoggedIn: (UserView) -> Unit
) {
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val modalBottomSheetAlert = rememberModalBottomSheetAlert()

    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.also { data ->
            onIntent(LoginIntent.ParseGoogleSignResult(intent = data))
        }
    }

    val loginAvatarImage = ImageBitmap
        .imageResource(id = R.drawable.login_background)
    val backgroundImage = ImageBitmap
        .imageResource(id = R.drawable.login_anim_background)

    val bkgAnim = rememberBkgAnimation()
    val screenWidthPx = LocalContext.current.resources.displayMetrics.widthPixels
    var showInteractionControls by rememberSaveable { mutableStateOf(false) }
    val interactionControlsAlpha by animateFloatAsState(
        targetValue = if (!showInteractionControls) 0f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.colors.material.surface),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                bitmap = backgroundImage,
                alpha = .7f,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(ratio = 1f, matchHeightConstraintsFirst = true)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, placeable.height) {
                            if (!bkgAnim.isStarted) {
                                bkgAnim.target = placeable.width - screenWidthPx
                            }
                            val anchor = object {
                                val w = placeable.width
                                var x = (w - screenWidthPx) / 2
                            }
                            placeable.placeRelative(
                                x = anchor.x - bkgAnim.offset,
                                y = 0
                            )
                        }
                    }
            )
            Image(
                bitmap = loginAvatarImage,
                alpha = interactionControlsAlpha,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.2f)
                .alpha(alpha = interactionControlsAlpha)
                .clip(
                    RoundedCornerShape(
                        topStart = AppTheme.Dimens.shapeCorner.medium * 8f,
                        topEnd = AppTheme.Dimens.shapeCorner.medium * 8f
                    )
                )
                .background(
                    color = AppTheme.colors.material.onSurface.copy(
                        alpha = .1f
                    )
                )
                .padding(AppTheme.Dimens.shapeCorner.medium * 8f)
        ) {
            SignInGoogleButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .height(SocialMediaButtonDefaults.height),
                onClick = {
                    onIntent(LoginIntent.PerformFirebaseGoogleSignIn)
                }
            )
        }
        ContentLoadingProgressBar(visible = isLoading)
    }

    LaunchedEffect(Unit) {
        uiState.collect { state ->
            when (state) {
                is AppState.Loading -> state.apply {
                    isLoading = true
                }
                is AppState.Loaded -> state.apply {
                    isLoading = false
                }
                is AppState.Success -> state.apply {
                    when (content) {
                        is LoginState.PerformGoogleSignIn -> activityResultLauncher.launch(
                            content.intent
                        )
                        is LoginState.ShowSignedInUser -> onSuccessfulLoggedIn(
                            content.user
                        )
                    }
                }
                is AppState.Error -> state.apply {
                    modalBottomSheetAlert
                        .showGenericErrorAlert(callId = R.id.login_generic_error)
                }
                else -> Unit
            }
            val isIdle = state is AppState.Idle
            val success = state as? AppState.Success<LoginState>
            val isShowSignedInUser = success?.content is LoginState.ShowSignedInUser
            val isPerformGoogleSignIn = success?.content is LoginState.PerformGoogleSignIn
            showInteractionControls = !isLoading && !isIdle && !isShowSignedInUser &&
                    !isPerformGoogleSignIn
        }
    }

    ModalBottomSheetAlertEffect(modalAlert = modalBottomSheetAlert) {
        asClickedState(R.id.login_generic_error) {
            hide()
            when (this) {
                is ModalBottomSheetAlertResultState.PositiveClicked ->
                    onIntent(LoginIntent.PerformFirebaseGoogleSignIn)
                else -> Unit
            }
        }
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewLoginDarkScreen() {
    AppTheme {
        LoginScreen(
            uiState = flowOf(AppState.Loading()),
            onIntent = { },
            onSuccessfulLoggedIn = { }
        )
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewLoginLightScreen() {
    AppTheme {
        LoginScreen(
            uiState = flowOf(AppState.Loading()),
            onIntent = { },
            onSuccessfulLoggedIn = { }
        )
    }
}