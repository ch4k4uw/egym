package com.ch4k4uw.workout.egym.training.plan.list.ui.component

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

@Stable
interface TopBarStateHolder {
    val topBarHeight: Int
    val topBarOffset: Float
    val topBarTransition: Float

    val nestedScrollConnection: NestedScrollConnection

    fun update(topBarHeight: Int)
}

@Stable
private class TopBarStateHolderImpl : TopBarStateHolder {
    companion object {
        val Saver: Saver<TopBarStateHolder, *> = Saver(
            save = {
                Bundle().apply {
                    putInt("tbh", it.topBarHeight)
                    putFloat("tbo", it.topBarOffset)
                }
            },
            restore = {
                TopBarStateHolderImpl().apply {
                    topBarHeight = it.getInt("tbh")
                    topBarOffset = it.getFloat("tbo")
                }
            }
        )
    }

    override var topBarHeight: Int by mutableStateOf(0)
        private set

    override var topBarOffset: Float by mutableStateOf(0f)
        private set

    override val topBarTransition: Float
        get() = -topBarOffset / topBarHeight

    override val nestedScrollConnection: NestedScrollConnection = object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            val delta = available.y
            val newOffset = topBarOffset + delta
            topBarOffset = newOffset.coerceIn(
                minimumValue = -topBarHeight.toFloat(), maximumValue = 0f
            )
            return if (
                topBarOffset == 0f ||
                topBarOffset == -topBarHeight.toFloat()
            ) {
                Offset.Zero
            } else {
                available
            }
        }
    }

    override fun update(topBarHeight: Int) {
        this.topBarHeight = topBarHeight
        topBarOffset = topBarOffset.coerceIn(
            minimumValue = -topBarHeight.toFloat(), maximumValue = 0f
        )
    }

}

@Composable
fun rememberSaveableTopBarStateHolder() = rememberSaveable(saver = TopBarStateHolderImpl.Saver) {
    TopBarStateHolderImpl()
}