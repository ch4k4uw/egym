package com.ch4k4uw.workout.egym.exercise.list.ui.component

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.ch4k4uw.workout.egym.R
import com.ch4k4uw.workout.egym.core.ui.AppTheme

private const val SelectionBackgroundAlpha = .4f
private const val PlaceholderTextAlpha = .5f

@ExperimentalUnitApi
@ExperimentalComposeUiApi
@Composable
fun ExerciseListTopSearchBar(
    query: String,
    modifier: Modifier,
    onNavigationClick: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onExecuteSearch: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    var textFieldValue by rememberSaveable(saver = textFieldValueSaver()) {
        mutableStateOf(
            TextFieldValue(
                text = query,
                selection = TextRange(start = 0, end = query.length)
            )
        )
    }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = AppTheme.colors.material.onSecondary,
        backgroundColor = AppTheme.colors.material.onSecondary.copy(
            alpha = SelectionBackgroundAlpha
        )
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        TopAppBar(
            modifier = modifier,
            backgroundColor = AppTheme.colors.material.secondary,
            navigationIcon = {
                IconButton(onClick = { onNavigationClick() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            },
            title = {
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester = focusRequester),
                    value = textFieldValue,
                    onValueChange = {
                        if (it.text != textFieldValue.text) {
                            onQueryChanged(it.text)
                        }
                        textFieldValue = it
                    },
                    placeholder = {
                        Text(
                            text = stringResource(
                                R.string.exercise_list_top_search_bar_search_placeholder
                            ),
                            style = AppTheme.typography.material.h6.copy(
                                color = AppTheme.colors.material.onSecondary.copy(
                                    alpha = PlaceholderTextAlpha
                                )
                            )
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onExecuteSearch(textFieldValue.text)
                            keyboardController?.hide()
                        },
                    ),
                    trailingIcon = {
                        if (textFieldValue.text.isBlank()) {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = stringResource(
                                    R.string.exercise_list_top_search_bar_search_icon_description
                                ),
                                tint = AppTheme.colors.material.onSecondary
                            )
                        } else {
                            IconButton(onClick = {
                                textFieldValue = textFieldValue.copy(text = "")
                                onQueryChanged("")
                            }) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = stringResource(
                                        R.string.exercise_list_top_search_bar_clear_icon_description
                                    ),
                                    tint = AppTheme.colors.material.onSecondary
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        cursorColor = AppTheme.colors.material.onSecondary
                    ),
                    singleLine = true
                )
            }
        )
    }

    SideEffect {
        focusRequester.requestFocus()
    }
}

@Suppress("unchecked_cast")
fun textFieldValueSaver(): Saver<MutableState<TextFieldValue>, *> = Saver(
    save = {
        Bundle().apply {
            putString("text", it.value.text)
            putSerializable("sel", Pair(it.value.selection.start, it.value.selection.end))
        }
    },
    restore = {
        val text = it.getString("text").orEmpty()
        val selection = it.getSerializable("sel") as? Pair<Int, Int>
            ?: Pair(text.length, text.length)
        mutableStateOf(
            TextFieldValue(
                text = text,
                selection = TextRange(start = selection.first, end = selection.second)
            )
        )
    }
)