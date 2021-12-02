package com.ch4k4uw.workout.egym

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.core.view.WindowCompat
import com.ch4k4uw.workout.egym.common.ui.theme.EGymTheme
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@ExperimentalUnitApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @FlowPreview
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            AppTheme {
                EGymTheme {
                    Navigation()
                }
            }
        }
    }
}