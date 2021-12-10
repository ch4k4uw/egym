package com.ch4k4uw.workout.egym.core.ui

import android.os.Bundle
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues

@Stable
class AppInsetsPaddingValues internal constructor(
    private val statusBarPaddingValues: PaddingValues,
    private val navigationBarPaddingValues: PaddingValues,
    private val layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    private var applyTop: Boolean = false,
    private var applyBottom: Boolean = false,
    private var applyStart: Boolean = false,
    private var applyEnd: Boolean = false,
) {
    private val mutablePaddingValues = mutableStateOf(createPaddingValues())
    val paddingValues: State<PaddingValues> = mutablePaddingValues

    fun enableInsets(
        statusBar: Boolean = true,
        navBar: Boolean = true
    ) {
        enableStatusBarPadding(enable = statusBar)
        enableNavigationBarsPadding(enable = navBar)
        mutablePaddingValues.value = createPaddingValues()
    }

    private fun enableStatusBarPadding(enable: Boolean = true) {
        applyTop = enable
    }

    private fun createPaddingValues(): PaddingValues {
        val top = if (applyTop) 1 else 0
        val bottom = if (applyBottom) 1 else 0
        val start = if (applyStart) 1 else 0
        val end = if (applyEnd) 1 else 0
        return PaddingValues(
            top = statusBarPaddingValues.calculateTopPadding() * top,
            bottom = navigationBarPaddingValues.calculateBottomPadding() * bottom,
            start = navigationBarPaddingValues.calculateStartPadding(
                layoutDirection = layoutDirection
            ) * start,
            end = navigationBarPaddingValues.calculateEndPadding(
                layoutDirection = layoutDirection
            ) * end
        )
    }

    private fun enableNavigationBarsPadding(enable: Boolean = true) {
        applyStart = enable
        applyEnd = enable
        applyBottom = enable
    }

    companion object {
        fun Saver(
            statusBarPaddingValues: PaddingValues,
            navigationBarPaddingValues: PaddingValues,
            layoutDirection: LayoutDirection = LayoutDirection.Ltr
        ): Saver<AppInsetsPaddingValues, *> = Saver(
            save = {
                Bundle().apply {
                    putBoolean("top", it.applyTop)
                    putBoolean("bottom", it.applyBottom)
                    putBoolean("start", it.applyStart)
                    putBoolean("end", it.applyEnd)
                }
            },
            restore = {
                val top = it.getBoolean("top")
                val bottom = it.getBoolean("bottom")
                val start = it.getBoolean("start")
                val end = it.getBoolean("end")
                AppInsetsPaddingValues(
                    statusBarPaddingValues = statusBarPaddingValues,
                    navigationBarPaddingValues = navigationBarPaddingValues,
                    layoutDirection = layoutDirection,
                    applyTop = top,
                    applyBottom = bottom,
                    applyStart = start,
                    applyEnd = end
                )
            }
        )
    }
}

@Composable
internal fun rememberAppInsetsPaddingValues(
    layoutDirection: LayoutDirection = LocalLayoutDirection.current
): AppInsetsPaddingValues {
    val statusBarPaddings = rememberInsetsPaddingValues(
        insets = LocalWindowInsets.current.statusBars,
        applyTop = true
    )
    val navigationBarPaddings = rememberInsetsPaddingValues(
        insets = LocalWindowInsets.current.navigationBars,
        applyStart = true,
        applyEnd = true,
        applyBottom = true,
    )
    return rememberSaveable(
        saver = AppInsetsPaddingValues.Saver(
            statusBarPaddingValues = statusBarPaddings,
            navigationBarPaddingValues = navigationBarPaddings,
            layoutDirection = layoutDirection
        )
    ) {
        AppInsetsPaddingValues(
            statusBarPaddingValues = statusBarPaddings,
            navigationBarPaddingValues = navigationBarPaddings,
            layoutDirection = layoutDirection
        )
    }
}