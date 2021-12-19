package com.ch4k4uw.workout.egym.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import com.ch4k4uw.workout.egym.core.ui.AppTheme
import com.ch4k4uw.workout.egym.login.interaction.UserView

@Composable
fun GenericTopAppBar(
    modifier: Modifier,
    title: String,
    actionIcons: (@Composable RowScope.() -> Unit)? = null,
    userData: UserView? = null,
    queryText: String = "",
    onNavigateBack: () -> Unit,
    onSearchButtonClick: (() -> Unit)? = null,
    onLogoutClick: () -> Unit = {},
) {
    val isProfileDialogShowing = remember { mutableStateOf(false) }
    TopAppBar(
        modifier = modifier,
        title = {
            Column {
                Text(
                    text = title,
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
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
            }
        },
        actions = {
            if (onSearchButtonClick != null) {
                IconButton(onClick = onSearchButtonClick) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                }
            }
            if (actionIcons != null) {
                actionIcons()
            } else {
                if (userData != null) {
                    IconButton(onClick = { isProfileDialogShowing.value = true }) {
                        RemoteIcon(
                            url = userData.image,
                            default = Icons.Filled.Person,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(all = AppTheme.Dimens.spacing.tiny)
                                .clip(CircleShape)
                                .background(color = AppTheme.colors.material.onPrimary)
                        )
                    }
                }
            }
        }
    )

    if (isProfileDialogShowing.value && userData != null) {
        ProfileDialog(
            image = userData.image,
            name = userData.name,
            email = userData.email,
            onDismissRequest = { isProfileDialogShowing.value = false },
            onLogout = onLogoutClick
        )
    }
}