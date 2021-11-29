package com.ch4k4uw.workout.egym.exercise.ui.component

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ch4k4uw.workout.egym.core.ui.AppTheme

@ExperimentalUnitApi
@Composable
fun ExerciseHeadCard(
    imageUrl: String,
    title: String,
    placeHolderImage: ImageVector = Icons.Filled.Image,
    errorImage: ImageVector = Icons.Filled.BrokenImage
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val imageHeight = maxWidth * .7f

        Column {
            Card(elevation = 3.dp, shape = RectangleShape) {
                Column {
                    var image by remember { mutableStateOf<Bitmap?>(null) }
                    var error by remember { mutableStateOf(false) }

                    if (imageUrl.isNotBlank()) {
                        Glide
                            .with(LocalContext.current)
                            .asBitmap()
                            .load(imageUrl)
                            .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap>?
                                ) {
                                    image = resource
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                }

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    error = true
                                }
                            })
                    }

                    if (!error) {
                        val bitmap = image
                        if (bitmap != null) {
                            var colorPalette by remember { mutableStateOf<Palette?>(null) }
                            var calculatingColorPalette = remember { false }
                            if (colorPalette == null && !calculatingColorPalette) {
                                true.also { calculatingColorPalette = it }
                                Palette.from(bitmap).generate {
                                    colorPalette = it
                                }
                            }
                            val backgroundColor = colorPalette?.let {
                                if (AppTheme.colors.material.isLight) {
                                    it.vibrantSwatch
                                } else {
                                    it.mutedSwatch
                                }
                            }?.rgb
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(imageHeight)
                                    .run {
                                        backgroundColor?.let { background(color = Color(it)) }
                                            ?: this
                                    }
                            )
                        } else {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(imageHeight)
                            ) {
                                Icon(imageVector = placeHolderImage, contentDescription = null)
                            }
                        }
                    } else {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(imageHeight)
                        ) {
                            Icon(imageVector = errorImage, contentDescription = null)
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = title, style = AppTheme.typography.material.h6)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}