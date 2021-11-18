package com.ch4k4uw.workout.egym.core.ui.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.core.R
import com.ch4k4uw.workout.egym.core.ui.AppTheme

@ExperimentalUnitApi
@Composable
fun SignInGoogleButton(
    modifier: Modifier = Modifier,
    content: String = "Sign in With Google",
    contentDescription: String = "Google Login",
    contentTextStyle: TextStyle = AppTheme.typography.material.subtitle2,
    contentHorizontalPadding: Dp = SocialMediaButtonDefaults.contentHorizontalPadding,
    onClick: () -> Unit
) {
    SocialMediaButton(
        modifier = modifier,
        content = content,
        icon = R.drawable.ic_google_logo,
        iconDescription = contentDescription,
        iconBackgroundColor = Color.White,
        contentTextStyle = contentTextStyle,
        contentHorizontalPadding = contentHorizontalPadding,
        onClick = onClick
    )
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewSignInGoogleDarkButton() {
    AppTheme {
        SignInGoogleButton(
            onClick = {
            }
        )
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewSignInGoogleLightButton() {
    AppTheme {
        SignInGoogleButton(
            onClick = {
            }
        )
    }
}