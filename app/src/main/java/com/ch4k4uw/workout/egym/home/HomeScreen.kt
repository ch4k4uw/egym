package com.ch4k4uw.workout.egym.home

import android.content.res.Configuration
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.core.ui.AppTheme

@ExperimentalUnitApi
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
             TopAppBar(
                 title = { Text(text = "eGym") }
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
        HomeScreen()
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewLightScreen() {
    AppTheme {
        HomeScreen()
    }
}