package com.ch4k4uw.workout.egym.login

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.login.domain.interactor.LoginInteractor
import com.ch4k4uw.workout.egym.login.extensions.toView
import com.ch4k4uw.workout.egym.login.interaction.LoginIntent
import com.ch4k4uw.workout.egym.login.interaction.LoginState
import com.ch4k4uw.workout.egym.common.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginInteractor: LoginInteractor
) : ViewModel() {
    private val mutableUiState = MutableSharedFlow<AppState<LoginState>>(replay = 1)
    val uiState: Flow<AppState<LoginState>> = mutableUiState

    init {
        viewModelScope.launch {
            emit(AppState.Loading())
            loginInteractor
                .findLoggedUser()
                .catch {
                    emit(AppState.Loaded())
                    emit(AppState.Error(cause = it))
                    it.printStackTrace()
                }
                .collect {
                    emit(AppState.Loaded())
                    if (it != User.Empty) {
                        emit(
                            AppState.Success(
                                content = LoginState.ShowSignedInUser(user = it.toView())
                            )
                        )
                    }
                }
        }
    }

    fun performIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.PerformFirebaseGoogleSignIn ->
                performGoogleLogin()
            is LoginIntent.ParseGoogleSignResult ->
                parseGoogleSignResult(intent.intent)
        }
    }

    private fun performGoogleLogin() {
        viewModelScope.launch {
            emit(
                AppState.Success(
                    LoginState.PerformGoogleSignIn(
                        intent = loginInteractor.findGoogleFbSignInIntent().single()
                    )
                )
            )
        }
    }

    private suspend fun <T : AppState<LoginState>> emit(value: T) =
        mutableUiState.emit(value)

    private fun parseGoogleSignResult(intent: Intent) {
        viewModelScope.launch {
            emit(AppState.Loading())
            loginInteractor
                .parseGoogleFbSignInResult(intent = intent)
                .catch {
                    emit(AppState.Loaded())
                    emit(AppState.Error(cause = it))
                    it.printStackTrace()
                }
                .collect {
                    emit(AppState.Loaded())
                    emit(
                        AppState.Success(
                            content = LoginState.ShowSignedInUser(user = it.toView())
                        )
                    )
                }
        }
    }
}