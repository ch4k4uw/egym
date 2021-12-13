package com.ch4k4uw.workout.egym.exercise.detail.ui.component

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlin.math.roundToInt

interface CollapsingTopBarStateHolder {
    val topBarMinHeightPx: Int
    val topBarMaxHeightPx: Int
    val topBarHeightOffsetPx: Float
    val topBarOffsetPx: Float

    val topBarHeightTransition: Float

    @get:Composable
    val topBarHeight: Dp

    val nestedScrollConnection: NestedScrollConnection

    fun update(minHeightPx: Int, maxHeightPx: Int)
}

@Stable
private class CollapsingTopBarStateHolderImpl : CollapsingTopBarStateHolder {
    companion object {
        val Saver: Saver<CollapsingTopBarStateHolder, *> = Saver(
            save = {
                Bundle().apply {
                    putInt("hmin", it.topBarMinHeightPx)
                    putInt("hmax", it.topBarMaxHeightPx)
                    putFloat("hoffset", it.topBarHeightOffsetPx)
                    putFloat("yoffset", it.topBarOffsetPx)
                }
            },
            restore = {
                CollapsingTopBarStateHolderImpl().apply {
                    topBarMinHeightPx = it.getInt("hmin")
                    topBarMaxHeightPx = it.getInt("hmax")
                    topBarHeightOffsetPx = it.getFloat("hoffset")
                    topBarOffsetPx = it.getFloat("yoffset")
                }
            }
        )
    }

    override var topBarMinHeightPx by mutableStateOf(0)
        private set
    override var topBarMaxHeightPx by mutableStateOf(0)
        private set
    override var topBarHeightOffsetPx by mutableStateOf(0f)
        private set
    override var topBarOffsetPx by mutableStateOf(0f)
        private set

    override val topBarHeightTransition: Float
        get() = ((topBarMaxHeightPx - topBarMinHeightPx) + topBarHeightOffsetPx) / (topBarMaxHeightPx - topBarMinHeightPx)

    override val topBarHeight: Dp
        @Composable
        get() = with(LocalDensity.current) {
            (topBarMaxHeightPx + topBarHeightOffsetPx.roundToInt()).toDp()
        }

    override val nestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.y
            val newHeightOffset = topBarHeightOffsetPx + topBarOffsetPx + delta
            topBarHeightOffsetPx = newHeightOffset.coerceIn(
                minimumValue = (-topBarMaxHeightPx + topBarMinHeightPx).toFloat(),
                maximumValue = 0f
            )
            val newOffset = topBarHeightOffsetPx + topBarOffsetPx +
                    topBarMaxHeightPx - topBarMinHeightPx + delta

            topBarOffsetPx = newOffset.coerceIn(
                minimumValue = -topBarMinHeightPx.toFloat(),
                maximumValue = 0f
            )
            return if (
                topBarHeightOffsetPx == 0f ||
                topBarOffsetPx == -topBarMinHeightPx.toFloat()
            ) {
                Offset.Zero
            } else {
                available
            }
        }
    }

    override fun update(minHeightPx: Int, maxHeightPx: Int) {
        topBarMinHeightPx = minHeightPx
        topBarMaxHeightPx = maxHeightPx
        topBarHeightOffsetPx = topBarHeightOffsetPx.coerceIn(
            minimumValue = (-topBarMaxHeightPx + topBarMinHeightPx).toFloat(),
            maximumValue = 0f
        )
        topBarOffsetPx = topBarOffsetPx.coerceIn(
            minimumValue = -topBarMinHeightPx.toFloat(),
            maximumValue = 0f
        )
    }
}

@Composable
fun rememberSaveableCollapsingTopBar() =
    rememberSaveable(saver = CollapsingTopBarStateHolderImpl.Saver) {
        CollapsingTopBarStateHolderImpl()
    }