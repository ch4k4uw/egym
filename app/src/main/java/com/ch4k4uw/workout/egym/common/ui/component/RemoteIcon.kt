package com.ch4k4uw.workout.egym.common.ui.component

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@Composable
fun RemoteIcon(
    url: String,
    default: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    var image by remember { mutableStateOf<Bitmap?>(null) }

    if (url.isNotBlank()) {
        Glide
            .with(LocalContext.current)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    image = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    val bitmap = image
    if (bitmap == null) {
        Icon(imageVector = default, contentDescription = contentDescription)
    } else {
        Icon(
            bitmap = bitmap.asImageBitmap(),
            modifier = modifier,
            contentDescription = contentDescription,
            tint = Color.Unspecified
        )
    }
}