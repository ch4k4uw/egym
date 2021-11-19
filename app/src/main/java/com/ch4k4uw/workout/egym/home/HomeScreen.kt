package com.ch4k4uw.workout.egym.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.core.ui.components.ContentLoadingProgressBar
import com.ch4k4uw.workout.egym.extensions.HandleEvent
import com.ch4k4uw.workout.egym.extensions.asSuccess
import com.ch4k4uw.workout.egym.extensions.handleSuccess
import com.ch4k4uw.workout.egym.extensions.isLoading
import com.ch4k4uw.workout.egym.home.interaction.HomeIntent
import com.ch4k4uw.workout.egym.home.interaction.HomeState
import com.ch4k4uw.workout.egym.state.AppState
import com.google.accompanist.insets.statusBarsPadding

@ExperimentalUnitApi
@Composable
fun HomeScreen(
    uiState: State<AppState<HomeState>>,
    onIntent: (HomeIntent) -> Unit = {},
    onLoggedOut: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    var userName by remember { mutableStateOf("") }
    uiState.asSuccess()?.also { state ->
        when (state.content) {
            is HomeState.DisplayUserData -> userName = state.content.user.name
            else -> Unit
        }
    }

    uiState.HandleEvent {
        handleSuccess {
            when(content) {
                is HomeState.ShowLoginScreen -> onLoggedOut()
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .statusBarsPadding(),
                    title = { Text(text = stringResource(id = R.string.home_screen_label, userName)) },
                    navigationIcon = {
                        IconButton(onClick = { onNavigateBack() }) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                        }
                    },
                    actions = {
                        IconButton(onClick = { onIntent(HomeIntent.PerformLogout) }) {
                            Icon(imageVector = Icons.Filled.Logout, contentDescription = "")
                        }
                    }
                )
            },
            content = {
            }
        )
        ContentLoadingProgressBar(visible = uiState.isLoading)
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDarkScreen() {
    AppTheme {
        HomeScreen(
            uiState = remember { mutableStateOf(AppState.Loading()) }
        )
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewLightScreen() {
    AppTheme {
        HomeScreen(
            uiState = remember { mutableStateOf(AppState.Loading()) }
        )
    }
}