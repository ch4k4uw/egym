package com.ch4k4uw.workout.egym.login

import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.ch4k4uw.workout.egym.common.BaseAppStateViewModel
import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.login.domain.interactor.LoginInteractor
import com.ch4k4uw.workout.egym.login.extensions.toView
import com.ch4k4uw.workout.egym.login.interaction.LoginIntent
import com.ch4k4uw.workout.egym.login.interaction.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginInteractor: LoginInteractor
) : BaseAppStateViewModel<LoginState, LoginIntent>() {
    init {
        viewModelScope.launch {
            emitLoading()
            loginInteractor
                .findLoggedUser()
                .catch {
                    emitLoaded()
                    emitError(cause = it)
                    it.printStackTrace()
                }
                .collect {
                    emitLoaded()
                    if (it != User.Empty) {
                        emitSuccess(value = LoginState.ShowSignedInUser(user = it.toView()))
                    }
                }
        }
    }

    override fun performIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.PerformFirebaseGoogleSignIn ->
                performGoogleLogin()
            is LoginIntent.ParseGoogleSignResult ->
                parseGoogleSignResult(intent.intent)
        }
    }

    private fun performGoogleLogin() {
        viewModelScope.launch {
            emitSuccess(
                LoginState.PerformGoogleSignIn(
                    intent = loginInteractor.findGoogleFbSignInIntent().single()
                )
            )
        }
    }

    private fun parseGoogleSignResult(intent: Intent) {
        viewModelScope.launch {
            emitLoading()
            loginInteractor
                .parseGoogleFbSignInResult(intent = intent)
                .catch {
                    emitLoaded()
                    emitError(cause = it)
                    it.printStackTrace()
                }
                .collect {
                    emitLoaded()
                    emitSuccess(value = LoginState.ShowSignedInUser(user = it.toView()))
                }
        }
    }
}