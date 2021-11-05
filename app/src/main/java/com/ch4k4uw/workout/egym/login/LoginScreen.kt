package com.ch4k4uw.workout.egym.login

import android.content.res.Configuration
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.core.ui.components.ContentLoadingProgressBar
import com.ch4k4uw.workout.egym.core.ui.components.SignInGoogleButton
import com.ch4k4uw.workout.egym.core.ui.components.SocialMediaButtonDefaults
import com.ch4k4uw.workout.egym.extensions.isLoading
import com.ch4k4uw.workout.egym.login.interaction.LoginIntent
import com.ch4k4uw.workout.egym.login.interaction.LoginState
import com.ch4k4uw.workout.egym.login.interaction.UserView
import com.ch4k4uw.workout.egym.state.AppState
import com.google.accompanist.insets.navigationBarsPadding

@ExperimentalUnitApi
@Composable
fun LoginScreen(
    uiState: State<AppState<LoginState>>,
    onIntent: (LoginIntent) -> Unit,
    onSuccessfulLoggedIn: (UserView) -> Unit
) {
    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        result.data?.also { data ->
            onIntent(LoginIntent.ParseGoogleSignResult(intent = data))
        }
    }

    (uiState.value as? AppState.Success<*>)
        ?.also {
            when(it.content) {
                is LoginState.PerformGoogleSignIn -> activityResultLauncher.launch(
                    it.content.intent
                )
                is LoginState.ShowSignedInUser -> onSuccessfulLoggedIn(
                    it.content.user
                )
            }
        }

    val image = ImageBitmap
        .imageResource(id = R.drawable.login_background)
    val animImage = ImageBitmap
        .imageResource(id = R.drawable.login_anim_background)

    val bkgAnim = rememberBkgAnimation()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = AppTheme.colors.material.surface)
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                bitmap = animImage,
                alpha = .7f,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(ratio = 1f, matchHeightConstraintsFirst = true)
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints)
                        layout(placeable.width, placeable.height) {
                            if (!bkgAnim.isStarted) {
                                bkgAnim.target = placeable.width / 2
                            }
                            placeable.placeRelative(
                                x = (-placeable.width / 4) + bkgAnim.offset,
                                y = 0
                            )
                        }
                    }
            )
            Image(
                bitmap = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        if (!uiState.isLoading) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.2f)
                    .clip(
                        RoundedCornerShape(
                            topStart = AppTheme.Dimens.shapeCorner.medium * 8f,
                            topEnd = AppTheme.Dimens.shapeCorner.medium * 8f
                        )
                    )
                    .background(color = AppTheme.colors.material.onSurface.copy(alpha = .1f))
                    .padding(AppTheme.Dimens.shapeCorner.medium * 8f)
            ) {
                SignInGoogleButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SocialMediaButtonDefaults.height),
                    onClick = {
                        onIntent(LoginIntent.PerformFirebaseGoogleSignIn)
                    }
                )
            }
        }
        ContentLoadingProgressBar(visible = uiState.isLoading)
    }
}

@Composable
fun rememberBkgAnimation(): BkgAnimation {
    var bkgAnimStarted by remember { mutableStateOf(false) }
    var xOffsetTarget by remember { mutableStateOf(0) }
    val xOffset: Int by animateIntAsState(
        targetValue = xOffsetTarget,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 120000),
            repeatMode = RepeatMode.Reverse
        )
    )

    return remember {
        BkgAnimation(
            {
                bkgAnimStarted
            },
            {
                if (it != null) {
                    xOffsetTarget = it
                    bkgAnimStarted = true
                }
                xOffsetTarget
            },
            {
                xOffset
            }
        )
    }
}

@Stable
class BkgAnimation(
    private val isAnimStartedGetter: () -> Boolean,
    private val xOffsetTargetGetterSetter: (Int?) -> Int,
    private val xOffsetGetter: () -> Int
) {
    val isStarted: Boolean
        get() = isAnimStartedGetter()

    var target: Int
        get() = xOffsetTargetGetterSetter(null)
        set(value) {
            xOffsetTargetGetterSetter(value)
        }

    val offset: Int
        get() = xOffsetGetter()
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewLoginDarkScreen() {
    AppTheme {
        LoginScreen(
            uiState = remember { mutableStateOf(AppState.Loading()) },
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
            uiState = remember { mutableStateOf(AppState.Loading()) },
            onIntent = { },
            onSuccessfulLoggedIn = { }
        )
    }
}