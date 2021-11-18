package com.ch4k4uw.workout.egym

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.core.view.WindowCompat
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.extensions.asUriString
import com.ch4k4uw.workout.egym.extensions.toParcelable
import com.ch4k4uw.workout.egym.login.interaction.UserView
import com.ch4k4uw.workout.egym.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalUnitApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val user = UserView("1", "2", "3", "4")
        val user64 = user.asUriString
        val user2 = user64.toParcelable<UserView>()

        setContent {
            AppTheme {
                Navigation()
            }
        }
    }
}