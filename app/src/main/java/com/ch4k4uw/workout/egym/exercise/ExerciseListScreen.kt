package com.ch4k4uw.workout.egym.exercise

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.common.ui.component.ProfileDialog
import com.ch4k4uw.workout.egym.common.ui.component.RemoteIcon
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.core.ui.components.ListLoadingShimmer1
import com.ch4k4uw.workout.egym.core.ui.components.ShimmerCardListItem2
import com.ch4k4uw.workout.egym.exercise.interaction.ExerciseHeadView
import com.ch4k4uw.workout.egym.exercise.interaction.ExerciseListIntent
import com.ch4k4uw.workout.egym.exercise.interaction.ExerciseListState
import com.ch4k4uw.workout.egym.exercise.ui.component.ExerciseHeadCard
import com.ch4k4uw.workout.egym.extensions.handleError
import com.ch4k4uw.workout.egym.extensions.handleSuccess
import com.ch4k4uw.workout.egym.extensions.raiseEvent
import com.ch4k4uw.workout.egym.login.interaction.UserView
import com.ch4k4uw.workout.egym.state.AppState
import com.google.accompanist.insets.statusBarsPadding

@ExperimentalUnitApi
@Composable
fun ExerciseListScreen(
    uiState: State<AppState<ExerciseListState>>,
    onIntent: (ExerciseListIntent) -> Unit = {},
    onLoggedOut: () -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    val uiStateValue = uiState.value
    var userData by remember { mutableStateOf(UserView.Empty) }
    var isProfileDialogShowing by remember { mutableStateOf(false) }
    var exercisesHeads by remember { mutableStateOf(listOf<ExerciseHeadView>()) }
    var showShimmer by remember { mutableStateOf(true) }

    uiState.raiseEvent().apply {
        handleSuccess {
            when (content) {
                is ExerciseListState.DisplayUserData -> userData = content.user
                is ExerciseListState.ShowLoginScreen -> onLoggedOut()
                is ExerciseListState.ShowExerciseList -> exercisesHeads =
                    mutableListOf(*content.exercises.toTypedArray())
                is ExerciseListState.ShowNoMorePagesToFetch -> showShimmer = false
                else -> Unit
            }
        }
        handleError {
            showShimmer = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier
                        .statusBarsPadding(),
                    title = {
                        Text(
                            text = stringResource(id = R.string.exercise_list_title),
                            style = AppTheme.typography.material.h6
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onNavigateBack() }) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                        }
                    },
                    actions = {
                        IconButton(onClick = { isProfileDialogShowing = true }) {
                            RemoteIcon(
                                url = userData.image,
                                default = Icons.Filled.Person,
                                contentDescription = "",
                                modifier = Modifier
                                    .padding(all = 4.dp)
                                    .clip(CircleShape)
                                    .background(color = AppTheme.colors.material.onPrimary)
                            )
                        }
                    }
                )
            },
            content = {
                if (uiStateValue is AppState.Loading<*>) {
                    if (uiStateValue.tag is ExerciseListState.ExerciseListTag) {
                        if (exercisesHeads.isEmpty()) {
                            ListLoadingShimmer1()
                        }
                    }
                }
                if(exercisesHeads.isNotEmpty()) {
                    LazyColumn {
                        val exercisesCount = exercisesHeads.size
                        items(count = exercisesCount, key = { exercisesHeads[it].id }) { index ->
                            ExerciseHeadCard(
                                imageUrl = exercisesHeads[index].image,
                                title = exercisesHeads[index].title
                            )
                        }
                        if (showShimmer) {
                            item {
                                ShimmerCardListItem2 {
                                    onIntent(ExerciseListIntent.FetchNextPage)
                                }
                            }
                        }

                    }
                }
            }
        )
        if (isProfileDialogShowing) {
            ProfileDialog(
                image = userData.image,
                name = userData.name,
                email = userData.email,
                onDismissRequest = { isProfileDialogShowing = false },
                onLogout = { onIntent(ExerciseListIntent.PerformLogout) }
            )
        }
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDarkScreen() {
    AppTheme {
        ExerciseListScreen(
            uiState = remember { mutableStateOf(AppState.Loading()) }
        )
    }
}

@ExperimentalUnitApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewLightScreen() {
    AppTheme {
        ExerciseListScreen(
            uiState = remember { mutableStateOf(AppState.Loading()) }
        )
    }
}