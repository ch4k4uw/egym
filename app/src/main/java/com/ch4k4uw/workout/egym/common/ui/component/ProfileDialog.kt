package com.ch4k4uw.workout.egym.common.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.core.ui.AppTheme

@ExperimentalUnitApi
@Composable
fun ProfileDialog(
    image: String,
    name: String,
    email: String,
    onDismissRequest: () -> Unit,
    onLogout: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    modifier = Modifier
                        .then(Modifier.size(Icons.Filled.Close.defaultWidth)),
                    onClick = { onDismissRequest() }
                ) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "")
                }
                Text(
                    text = stringResource(id = R.string.app_name),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        },
        text = {
            Text(text = "")//Workaround: Used as anchor
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RemoteIcon(
                        url = image,
                        default = Icons.Filled.Person,
                        contentDescription = "",
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(color = AppTheme.colors.material.onPrimary)
                            .border(
                                border = BorderStroke(
                                    1.dp,
                                    AppTheme.colors.material.onSurface
                                ),
                                shape = CircleShape
                            )
                    )
                    Column(
                        modifier = Modifier
                            .padding(
                                start = AppTheme.Dimens.spacing.normal
                            )
                    ) {
                        Text(
                            text = name,
                            style = AppTheme.typography.material.body1
                        )
                        Text(
                            text = email,
                            style = AppTheme.typography.material.body2
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(AppTheme.Dimens.spacing.tiny)
                )
            }
        },
        buttons = {
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppTheme.Dimens.spacing.xxnormal
                    )
                    .padding(
                        bottom = AppTheme.Dimens.spacing.xxnormal
                    )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Logout,
                        modifier = Modifier
                            .padding(
                                end = AppTheme.Dimens.spacing.xtiny
                            ),
                        contentDescription = ""
                    )
                    Text(
                        text = stringResource(
                            id = R.string.profile_dialog_exit_button_label
                        )
                    )
                }
            }
        }
    )
}