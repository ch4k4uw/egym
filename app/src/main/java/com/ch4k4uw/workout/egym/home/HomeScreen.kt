package com.ch4k4uw.workout.egym.home

import android.content.res.Configuration
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.home.interaction.HomeState
import com.ch4k4uw.workout.egym.state.AppState
import com.google.accompanist.insets.statusBarsPadding

@ExperimentalUnitApi
@Composable
fun HomeScreen(
    uiState: State<AppState<HomeState>>
) {
    var userName by remember { mutableStateOf("") }
    (uiState.value as? AppState.Success)?.also { state ->
        when (state.content) {
            is HomeState.DisplayUserData -> userName = " - ${state.content.user.name}"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .statusBarsPadding(),
                title = { Text(text = "eGym$userName") }
            )
        },
        content = {
        }
    )
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDarkScreen() {
    AppTheme {
        HomeScreen(
            uiState = remember { mutableStateOf(AppState.Idle()) }
        )
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewLightScreen() {
    AppTheme {
        HomeScreen(
            uiState = remember { mutableStateOf(AppState.Idle()) }
        )
    }
}