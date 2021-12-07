package com.ch4k4uw.workout.egym.exercise.list.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.common.ui.component.RemoteIcon
import com.ch4k4uw.workout.egym.common.ui.theme.EGymTheme
import com.ch4k4uw.workout.egym.core.ui.AppTheme

@ExperimentalUnitApi
@Composable
fun ExerciseListTopAppBar(
    modifier: Modifier,
    queryText: String,
    profileImage: String,
    onNavigateBack: () -> Unit,
    onSearchButtonClick: () -> Unit,
    onProfileButtonClick: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Column {
                Text(
                    text = stringResource(id = R.string.exercise_list_title),
                    style = if (queryText.isBlank()) {
                        AppTheme.typography.material.h6
                    } else {
                        AppTheme.typography.material.subtitle1
                    }
                )
                if (queryText.isNotBlank()) {
                    Text(
                        text = queryText,
                        style = AppTheme.typography.material.subtitle2.copy(
                            color = AppTheme.colors.material.secondary
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
            }
        },
        actions = {
            IconButton(onClick = onSearchButtonClick) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            }
            IconButton(onClick = onProfileButtonClick) {
                RemoteIcon(
                    url = profileImage,
                    default = Icons.Filled.Person,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(all = AppTheme.Dimens.spacing.tiny)
                        .clip(CircleShape)
                        .background(color = AppTheme.colors.material.onPrimary)
                )
            }
        }
    )
}