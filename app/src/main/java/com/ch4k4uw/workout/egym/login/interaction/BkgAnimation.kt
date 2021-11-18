package com.ch4k4uw.workout.egym.login.interaction

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Stable
class BkgAnimation(
    private val isAnimStartedGetter: () -> Boolean,
    private val xOffsetTargetGetterSetter: (Int?) -> Int,
    private val xOffsetGetter: () -> Int
) {
    val isStarted: Boolean
        get() = isAnimStartedGetter()

    var target: Int
        get() = xOffsetTargetGetterSetter(null)
        set(value) {
            xOffsetTargetGetterSetter(value)
        }

    val offset: Int
        get() = xOffsetGetter()
}

@Composable
fun rememberBkgAnimation(): BkgAnimation {
    var bkgAnimStarted by remember { mutableStateOf(false) }
    var xOffsetTarget by remember { mutableStateOf(0) }
    val xOffset: Int by animateIntAsState(
        targetValue = xOffsetTarget,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 120000),
            repeatMode = RepeatMode.Reverse
        )
    )

    return remember {
        BkgAnimation(
            {
                bkgAnimStarted
            },
            {
                if (it != null) {
                    xOffsetTarget = it
                    bkgAnimStarted = true
                }
                xOffsetTarget
            },
            {
                xOffset
            }
        )
    }
}