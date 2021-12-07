package com.ch4k4uw.workout.egym.exercise.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.common.ui.theme.EGymTheme
import com.ch4k4uw.workout.egym.core.extensions.rememberBitmapLoader
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.core.ui.components.ShimmerRectangle1
import com.ch4k4uw.workout.egym.exercise.detail.interaction.ExerciseDetailState
import com.ch4k4uw.workout.egym.exercise.detail.interaction.ExerciseView
import com.ch4k4uw.workout.egym.exercise.detail.ui.component.rememberSaveableCollapsingTopBar
import com.ch4k4uw.workout.egym.exercise.interaction.ExerciseViewSaver
import com.ch4k4uw.workout.egym.extensions.asError
import com.ch4k4uw.workout.egym.extensions.asSuccess
import com.ch4k4uw.workout.egym.extensions.isLoading
import com.ch4k4uw.workout.egym.extensions.raiseEvent
import com.ch4k4uw.workout.egym.state.AppState
import kotlin.math.roundToInt

private enum class LayoutId {
    TopBar,
    BackgroundImg,
    BackgroundImgShimmer,
    NavIcon,
    Title,
    TitleShimmer,
    Description,
    DescriptionShimmer
}

private val AppBarHeight = 56.dp
private val AppBarHorizontalPadding = 4.dp
private val TitleIconWith = 72.dp

@ExperimentalUnitApi
@Composable
fun ExerciseDetailScreen(
    uiState: State<AppState<ExerciseDetailState>>,
    onShowExerciseList: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val isLoading by remember {
        derivedStateOf {
            uiState.isLoading
        }
    }
    var exerciseDetail by rememberSaveable(saver = ExerciseViewSaver.Saver) {
        mutableStateOf(ExerciseView.Empty)
    }
    val coverUrl by remember {
        derivedStateOf {
            exerciseDetail.images.takeIf { it.isNotEmpty() }?.get(0) ?: ""
        }
    }

    val stateHolder = rememberSaveableCollapsingTopBar()

    uiState.value.asSuccess()?.apply {
        when (content) {
            is ExerciseDetailState.ShowDetail -> exerciseDetail = content.detail
            else -> Unit
        }
    }

    uiState.raiseEvent().asError()?.apply {
        onShowExerciseList()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val topBarHorizontalPaddingPx: Float
        val topBarIconWithPx: Float
        val topBarHeight: Dp
        val topBarHeightPx: Float
        val fontSize: TextUnit

        with(LocalDensity.current) {
            stateHolder.update(
                minHeightPx = AppBarHeight.toPx().roundToInt(),
                maxHeightPx = (maxWidth * .7f).toPx().roundToInt()
            )

            topBarHorizontalPaddingPx = AppBarHorizontalPadding.toPx()
            topBarIconWithPx = TitleIconWith.toPx()

            topBarHeight = stateHolder.topBarHeight
            topBarHeightPx = stateHolder.topBarHeight.toPx()

            fontSize = object {
                val h6 = AppTheme.typography.material.h6.fontSize.toPx()
                val h4 = AppTheme.typography.material.h4.fontSize.toPx()
                val currSz = (h6 + ((h4 - h6) * stateHolder.topBarHeightTransition)).toSp()
            }.currSz
        }

        Layout(
            modifier = Modifier
                .nestedScroll(connection = stateHolder.nestedScrollConnection),
            content = {
                Surface(
                    modifier = Modifier
                        .layoutId(LayoutId.TopBar)
                        .fillMaxWidth()
                        .height(height = topBarHeight)
                        .background(color = AppTheme.colors.material.primarySurface),
                    elevation = AppBarDefaults.TopAppBarElevation
                ) {
                    val image by rememberBitmapLoader(url = coverUrl)
                    if (image == null) {
                        ShimmerRectangle1(
                            height = topBarHeight,
                        )
                    } else {
                        val bitmap = image?.getOrNull()
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize(),
                                alpha = (1.7f * stateHolder.topBarHeightTransition)
                                    .coerceIn(0f, 1f)
                            )
                        } else {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.BrokenImage,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .layoutId(LayoutId.NavIcon)
                        .height(height = AppBarHeight)
                        .width(width = TitleIconWith - AppBarHorizontalPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { onNavigateBack() }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "",
                            tint = AppTheme.colors.material.onSurface
                        )
                    }
                }
                Text(
                    modifier = Modifier
                        .layoutId(LayoutId.Title)
                        .fillMaxWidth(),
                    text = exerciseDetail.title,
                    style = AppTheme.typography.material.h6.copy(
                        fontSize = fontSize
                    ),
                    color = AppTheme.colors.material.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (isLoading) {
                    ShimmerRectangle1(
                        layoutId = LayoutId.TitleShimmer,
                        height = with(LocalDensity.current) { fontSize.toDp() },
                    )
                }
                val scrollSate = rememberScrollState()
                if(!isLoading) {
                    Column(
                        modifier = Modifier
                            .layoutId(LayoutId.Description)
                            .fillMaxSize()
                            .padding(all = EGymTheme.Dimens.exerciseDetail.descriptionPadding)
                            .verticalScroll(state = scrollSate)
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = exerciseDetail.description,
                            style = AppTheme.typography.material.h6,
                            color = AppTheme.colors.material.onSurface,
                        )
                        Spacer(
                            modifier = Modifier
                                .height(
                                    height = EGymTheme
                                        .Dimens
                                        .exerciseDetail
                                        .descriptionDescriptionBottomPadding
                                )
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = stringResource(
                                R.string.exercise_detail_tags_label,
                                exerciseDetail.tags.joinToString { "#$it" }
                            ),
                            style = AppTheme.typography.material.h6,
                            color = AppTheme.colors.material.onSurface,
                        )
                    }
                }
                if (isLoading) {
                    Column(
                        modifier = Modifier
                            .layoutId(LayoutId.DescriptionShimmer)
                            .fillMaxSize()
                            .padding(all = EGymTheme.Dimens.exerciseDetail.descriptionPadding)
                            .verticalScroll(state = scrollSate)
                    ) {
                        for (i in 0..2) {
                            ShimmerRectangle1(
                                height = with(LocalDensity.current) {
                                    AppTheme.typography.material.h6.fontSize.toDp()
                                },
                                width = if (i == 2) {
                                    EGymTheme
                                        .Dimens
                                        .exerciseDetail
                                        .descriptionShimmerSmallestLineWidth
                                } else {
                                    Dp.Unspecified
                                }
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(
                                        height = EGymTheme
                                            .Dimens
                                            .exerciseDetail
                                            .descriptionPadding
                                    )
                            )
                        }
                    }
                }
            }
        ) { measure, constraints ->
            val indexes = hashMapOf<LayoutId, Int>().apply {
                measure.forEachIndexed { index, measurable ->
                    (measurable.layoutId as? LayoutId)?.also { this[it] = index }
                }
            }

            val placeable = measure.mapIndexed { i, it ->
                val isTitle = i == indexes[LayoutId.Title] || i == indexes[LayoutId.TitleShimmer]
                it.measure(
                    constraints = constraints.copy(
                        maxWidth = constraints.maxWidth - if (isTitle) {
                            topBarIconWithPx.roundToInt() + topBarHorizontalPaddingPx.roundToInt()
                        } else {
                            0
                        }
                    )
                )
            }
            val wMax = placeable.maxOf { it.width }
            val hMax = placeable.maxOf { it.height }

            val topBarPlaceable = indexes[LayoutId.TopBar]?.let(placeable::get)
            val navIconPlaceable = indexes[LayoutId.NavIcon]?.let(placeable::get)
            val titlePlaceable = indexes[LayoutId.Title]?.let(placeable::get)
            val titleShimmerPlaceable = indexes[LayoutId.TitleShimmer]?.let(placeable::get)
            val descriptionPlaceable = indexes[LayoutId.Description]?.let(placeable::get)
            val descriptionShimmerPlaceable = indexes[LayoutId.DescriptionShimmer]
                ?.let(placeable::get)

            layout(width = wMax, height = hMax) {
                val placeStack = Array<(() -> Unit)?>(LayoutId.values().size) { null }
                val yOffsetPx = stateHolder.topBarOffsetPx.roundToInt()
                val minTopBarHeightPx = stateHolder.topBarMinHeightPx

                topBarPlaceable?.also { topBar ->
                    placeStack[LayoutId.TopBar.ordinal] = {
                        topBar.place(x = 0, y = yOffsetPx)
                    }
                    navIconPlaceable?.also { navIcon ->
                        placeStack[LayoutId.NavIcon.ordinal] = {
                            navIcon.place(
                                x = topBarHorizontalPaddingPx.roundToInt(), y = yOffsetPx
                            )
                        }
                    }
                    titlePlaceable?.also { title ->
                        placeStack[LayoutId.Title.ordinal] = {
                            title.place(
                                x = topBarIconWithPx.roundToInt(),
                                y = (topBarHeightPx - (minTopBarHeightPx / 2 + title.height / 2))
                                    .roundToInt() + yOffsetPx
                            )
                        }
                    }
                    titleShimmerPlaceable?.also { title ->
                        placeStack[LayoutId.TitleShimmer.ordinal] = {
                            title.place(
                                x = topBarIconWithPx.roundToInt(),
                                y = (topBarHeightPx - (minTopBarHeightPx / 2 + title.height / 2))
                                    .roundToInt() + yOffsetPx
                            )
                        }
                    }
                    descriptionPlaceable?.also { description ->
                        placeStack[LayoutId.Description.ordinal] = {
                            description.place(
                                x = 0,
                                y = topBarHeightPx.roundToInt() + yOffsetPx
                            )
                        }
                    }
                    descriptionShimmerPlaceable?.also { description ->
                        placeStack[LayoutId.DescriptionShimmer.ordinal] = {
                            description.place(
                                x = 0,
                                y = topBarHeightPx.roundToInt() + yOffsetPx
                            )
                        }
                    }
                }

                LayoutId.values().forEach { placeStack[it.ordinal]?.invoke() }
            }
        }
    }
}