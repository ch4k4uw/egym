package com.ch4k4uw.workout.egym.core.auth.infra.service

import android.content.Intent
import com.ch4k4uw.workout.egym.core.auth.domain.entity.User
import com.ch4k4uw.workout.egym.core.auth.domain.service.ParseGoogleFirebaseSignInResultService
import com.ch4k4uw.workout.egym.core.common.infra.AppDispatchers
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.coroutines.suspendCoroutine

class ParseGoogleFirebaseSignInResultServiceImpl(
    private val fbAuth: FirebaseAuth,
    private val dispatchers: AppDispatchers
) : ParseGoogleFirebaseSignInResultService {
    override fun parse(intent: Intent): Flow<User> = flow {
        val signedInTask = GoogleSignIn.getSignedInAccountFromIntent(intent)
        val account = signedInTask.getResult(ApiException::class.java)!!
        val user = auth(account = account)

        emit(user)
    }.flowOn(dispatchers.io)

    private suspend fun auth(account: GoogleSignInAccount): User {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return suspendCoroutine { continuation ->
            fbAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    val result = if (task.isSuccessful) {
                        val rawUser = fbAuth.currentUser!!
                        Result.success(
                            User(
                                id = rawUser.uid,
                                name = rawUser.displayName.orEmpty(),
                                email = rawUser.email.orEmpty(),
                                image = rawUser.photoUrl?.toString().orEmpty()
                            )
                        )

                    } else {
                        Result.failure(task.exception ?: Exception())
                    }
                    continuation.resumeWith(result)
                }
        }
    }
}