package com.ch4k4uw.workout.egym.core.ui.color

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import com.ch4k4uw.workout.egym.core.ui.AppColors

@Suppress("unused")
internal object ColorConstants {
    private val BlueGray900Normal = Color(0xff263238)
    private val BlueGray900Light = Color(0xff4f5b62)
    private val BlueGray900Dark = Color(0xff000a12)
    private val Amber700Normal = Color(0xffffa000)
    private val Amber700Light = Color(0xffffd149)
    private val Amber700Dark = Color(0xffc67100)
    private val Amber50Normal = Color(0xfffff8e1)
    private val Red50Normal = Color(0xffB71C1C)
    private val BlueGray200Normal = Color(0xffb0bec5)
    private val BlueGray700Normal = Color(0xff455a64)
    private val Amber200Normal = Color(0xffffe082)
    private val DarkGrey = Color(0xff121212)
    private val DarkRed = Color(0xffcf6679)
    private val LightSurface = Color.White

    val LightColors: AppColors by lazy {
        AppColors(
            material = lightColors(
                primary = BlueGray900Normal,
                primaryVariant = BlueGray900Dark,
                secondary = Amber700Normal,
                secondaryVariant = Amber700Dark,
                background = Color.White,
                surface = LightSurface,
                error = Red50Normal,
                onPrimary = Amber50Normal,
                onSecondary = BlueGray900Normal,
                onBackground = Color.Black,
                onSurface = Color.Black,
                onError = Color.White
            ),
            googleButton = LightSurface
        )
    }

    private val DarkSurface = DarkGrey

    val DarkColors: AppColors by lazy {
        AppColors(
            material = darkColors(
                primary = BlueGray200Normal,
                primaryVariant = BlueGray700Normal,
                secondary = Amber200Normal,
                secondaryVariant = Amber700Normal,
                background = DarkGrey,
                surface = DarkSurface,
                error = DarkRed,
                onPrimary = Color.Black,
                onSecondary = Color.Black,
                onBackground = Color.White,
                onSurface = Color.White,
                onError = Color.Black
            ),
            googleButton = Color(color = 0xFF4285F4)
        )
    }

    val Colors.primaryLight: Color
        get() = if (isLight) BlueGray900Light else BlueGray200Normal

    val Colors.secondaryLight: Color
        get() = if (isLight) Amber700Light else Amber200Normal

}